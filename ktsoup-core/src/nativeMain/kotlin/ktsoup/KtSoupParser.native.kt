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

import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import lexbor.LXB_STATUS_OK
import lexbor.lxb_html_document_create
import lexbor.lxb_html_document_parse

public actual object KtSoupParser {

    public actual fun parse(html: String): KtSoupDocument = memScoped {
        val documentPointer = lxb_html_document_create()
        check(
            lxb_html_document_parse(
                documentPointer,
                html.cstr.ptr.reinterpret(),
                html.length.convert(),
            ) == LXB_STATUS_OK,
        ) { "Failed to parse HTML document." }
        KtSoupDocument(documentPointer)
    }
}
