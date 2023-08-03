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

import kotlinx.coroutines.runBlocking
import java.io.InputStream

internal class ChunkedInputStream(
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

internal class BlockingChunkedInputStream(
    bufferSize: Int,
    private val getChunk: suspend (buffer: ByteArray) -> Int,
) : InputStream() {

    private val buffer: ByteArray = ByteArray(bufferSize)
    private var bufferPos = 0
    private var bufferLimit = 0

    override fun read(): Int {
        if (bufferPos >= bufferLimit) {
            bufferLimit = runBlocking { getChunk(buffer) }
            bufferPos = 0

            if (bufferLimit == -1) {
                return -1 // end of stream
            }
        }

        return buffer[bufferPos++].toInt() and 0xff
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (bufferPos >= bufferLimit) {
            bufferLimit = runBlocking { getChunk(buffer) }
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
