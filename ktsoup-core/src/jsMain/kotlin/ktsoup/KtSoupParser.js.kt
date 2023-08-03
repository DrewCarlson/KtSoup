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

import ktsoup.nodehtmlparser.HTMLParser

public actual interface KtSoupParser {

    public actual companion object : KtSoupParser by KtSoupParserImpl() {
        public actual fun create(): KtSoupParser = KtSoupParserImpl()
    }

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

private class KtSoupParserImpl : KtSoupParser {

    override fun parse(html: String): KtSoupDocument {
        val document = checkNotNull(HTMLParser.parse(html)) {
            "Failed to parse HTML document"
        }
        return KtSoupDocument(document)
    }

    override fun parseChunked(
        bufferSize: Int,
        getChunk: (buffer: ByteArray) -> Int,
    ): KtSoupDocument {
        val buffer = ByteArray(bufferSize)
        var out = ByteArray(0)
        var totalBytes = 0

        var bytes = getChunk(buffer)
        while (bytes != -1) {
            out = out.copyOf(totalBytes + bytes)
            buffer.copyInto(out, totalBytes, 0, bytes)
            totalBytes += bytes

            bytes = getChunk(buffer)
        }
        return KtSoupDocument(HTMLParser.parse(out.decodeToString()))
    }

    override suspend fun parseChunkedAsync(
        bufferSize: Int,
        getChunk: suspend (buffer: ByteArray) -> Int,
    ): KtSoupDocument {
        val buffer = ByteArray(bufferSize)
        var out = ByteArray(0)
        var totalBytes = 0

        var bytes = getChunk(buffer)
        while (bytes != -1) {
            out = out.copyOf(totalBytes + bytes)
            buffer.copyInto(out, totalBytes, 0, bytes)
            totalBytes += bytes

            bytes = getChunk(buffer)
        }
        return KtSoupDocument(HTMLParser.parse(out.decodeToString()))
    }
}
