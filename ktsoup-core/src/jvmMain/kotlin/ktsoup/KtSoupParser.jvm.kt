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

public actual interface KtSoupParser {

    public actual companion object : KtSoupParser by KtSoupParserImpl() {
        public actual fun create(): KtSoupParser = KtSoupParserImpl()
    }

    /**
     * Parse an HTML document use the provided [inputStream], the stream will be
     * closed when reading is complete.
     *
     * @param inputStream The source [InputStream] to read.
     * @return The parsed document as a [KtSoupDocument].
     */
    public fun parse(inputStream: InputStream): KtSoupDocument

    public actual fun parse(html: String): KtSoupDocument

    public actual fun parseChunked(
        bufferSize: Int,
        getChunk: (buffer: ByteArray) -> Int,
    ): KtSoupDocument

    public actual suspend fun parseChunkedAsync(
        bufferSize: Int,
        getChunk: suspend (buffer: ByteArray) -> Int,
    ): KtSoupDocument
}

internal class KtSoupParserImpl : KtSoupParser {
    override fun parse(html: String): KtSoupDocument {
        return KtSoupDocument(Jsoup.parse(html))
    }

    override fun parseChunked(
        bufferSize: Int,
        getChunk: (buffer: ByteArray) -> Int,
    ): KtSoupDocument {
        val inputStream = ChunkedInputStream(bufferSize, getChunk)
        return KtSoupDocument(Jsoup.parse(inputStream, null, ""))
    }

    override suspend fun parseChunkedAsync(
        bufferSize: Int,
        getChunk: suspend (buffer: ByteArray) -> Int,
    ): KtSoupDocument {
        val inputStream = BlockingChunkedInputStream(bufferSize, getChunk)
        return KtSoupDocument(Jsoup.parse(inputStream, null, ""))
    }

    override fun parse(inputStream: InputStream): KtSoupDocument {
        return KtSoupDocument(Jsoup.parse(inputStream, null, ""))
    }
}
