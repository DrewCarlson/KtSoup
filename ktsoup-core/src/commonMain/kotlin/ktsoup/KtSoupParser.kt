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

/**
 * The default buffer size for parsing HTML documents using [KtSoupParser.parseChunked].
 */
public const val DEFAULT_PARSE_BUFFER_SIZE: Int = 1024

/**
 * The primary entrypoint for KtSoup, use [parse] to receive a [KtSoupDocument]
 * to access and manipulate the provided HTML document.
 */
public expect interface KtSoupParser {

    public companion object : KtSoupParser {
        /**
         * Create a new [KtSoupParser] instance.
         */
        public fun create(): KtSoupParser
    }

    /**
     * Parse the given [html] document.
     *
     * @param html The HTML document as a string.
     * @return The parsed document as a [KtSoupDocument].
     */
    public fun parse(html: String): KtSoupDocument

    /**
     * Parse an HTML document by calling [getChunk] to fill the provided [ByteArray]
     * until no more data is available.
     *
     * **Javascript Note:** node-html-parser only supports parsing strings, so the entire file
     * will be loaded into memory and parsed in one go.
     *
     * @param bufferSize The size of the buffer to provide to [getChunk].
     * @param getChunk A function to fill the provided buffer, returning the number of bytes or -1 when exhausted.
     * @return The parsed document as a [KtSoupDocument].
     */
    public fun parseChunked(
        bufferSize: Int = DEFAULT_PARSE_BUFFER_SIZE,
        getChunk: (buffer: ByteArray) -> Int,
    ): KtSoupDocument

    /**
     * Parse an HTML document by calling [getChunk] to fill the provided [ByteArray]
     * until no more data is available.
     *
     * **Javascript Note:** node-html-parser only supports parsing strings, so the entire file
     * will be loaded into memory and parsed in one go.
     *
     * @param bufferSize The size of the buffer to provide to [getChunk].
     * @param getChunk A function to fill the provided buffer, returning the number of bytes or -1 when exhausted.
     * @return The parsed document as a [KtSoupDocument].
     */
    public suspend fun parseChunkedAsync(
        bufferSize: Int = DEFAULT_PARSE_BUFFER_SIZE,
        getChunk: suspend (buffer: ByteArray) -> Int,
    ): KtSoupDocument
}
