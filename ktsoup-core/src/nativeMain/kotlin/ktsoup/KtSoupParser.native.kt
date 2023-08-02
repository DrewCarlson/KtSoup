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

import kotlinx.cinterop.*
import lexbor.*

public actual object KtSoupParser {

    public actual fun parse(html: String): KtSoupDocument = memScoped {
        val documentPointer = lxb_html_document_create()
        val status = lxb_html_document_parse(
            documentPointer,
            html.cstr.ptr.reinterpret(),
            html.length.convert(),
        )
        check(status == LXB_STATUS_OK) { "Failed to parse HTML document." }
        KtSoupDocument(documentPointer)
    }

    public actual fun parseChunked(
        bufferSize: Int,
        getChunk: (buffer: ByteArray) -> Int,
    ): KtSoupDocument = memScoped {
        val parser = lxb_html_parser_create()
        var status = lxb_html_parser_init(parser)
        defer { lxb_html_parser_destroy(parser) }
        check(status == LXB_STATUS_OK) { "Failed to initialize HTML parser." }

        val documentPtr = lxb_html_parse_chunk_begin(parser)
        val buffer = ByteArray(bufferSize)

        var byteCount = getChunk(buffer)
        while (byteCount != -1) {
            val bufferPtr = buffer.toCValues().ptr
            status = lxb_html_parse_chunk_process(parser, bufferPtr.reinterpret(), byteCount.toULong())
            check(status == LXB_STATUS_OK) { "Failed to process HTML chunk." }
            byteCount = getChunk(buffer)
        }

        status = lxb_html_parse_chunk_end(parser)
        check(status == LXB_STATUS_OK) { "Failed to parse HTML document." }

        return KtSoupDocument(documentPtr)
    }
}
