/**
 * KtSoup
 * Copyright (C) 2023 Drew Carlson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ktsoup

import org.jsoup.Jsoup
import java.io.InputStream

public actual object KtSoupParser {

    public actual fun parse(html: String): KtSoupDocument {
        return KtSoupDocument(Jsoup.parse(html))
    }

    public actual fun parseChunked(
        bufferSize: Int,
        getChunk: (buffer: ByteArray) -> Int,
    ): KtSoupDocument {
        val inputStream = ChunkedInputStream(bufferSize, getChunk)
        return KtSoupDocument(Jsoup.parse(inputStream, null, ""))
    }

    /**
     * Parse an HTML document use the provided [inputStream], the stream will be
     * closed when reading is complete.
     *
     * @param inputStream The source [InputStream] to read.
     * @return The parsed document as a [KtSoupDocument].
     */
    public fun parse(inputStream: InputStream): KtSoupDocument {
        return KtSoupDocument(Jsoup.parse(inputStream, null, ""))
    }
}

private class ChunkedInputStream(
    bufferSize: Int,
    private val getChunk: (buffer: ByteArray) -> Int,
) : InputStream() {

    private val buffer: ByteArray = ByteArray(bufferSize)
    private var bufferPos = 0
    private var bufferLimit = 0

    override fun read(): Int {
        if (bufferPos >= bufferLimit) {
            bufferLimit = getChunk(buffer)
            bufferPos = 0

            if (bufferLimit == -1) {
                return -1 // end of stream
            }
        }

        return buffer[bufferPos++].toInt() and 0xff
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (bufferPos >= bufferLimit) {
            bufferLimit = getChunk(buffer)
            bufferPos = 0

            if (bufferLimit == -1) {
                return -1 // end of stream
            }
        }

        val bytesRead = len.coerceAtMost(bufferLimit - bufferPos)
        System.arraycopy(buffer, bufferPos, b, off, bytesRead)
        bufferPos += bytesRead
        return bytesRead
    }
}
