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
import platform.posix.size_tVar

private const val START_LIST_SIZE = 128uL

public actual class KtSoupDocument {

    private var documentPointer: CPointer<lxb_html_document_t>? = null

    public actual fun parse(html: String): Boolean = memScoped {
        documentPointer = lxb_html_document_create()
        lxb_html_document_parse(
            checkDocument(),
            html.cstr.ptr.reinterpret(),
            html.length.convert(),
        ) == LXB_STATUS_OK
    }

    public actual fun close() {
        documentPointer ?: return
        lxb_html_document_destroy(checkDocument())
    }

    public actual fun title(): String = memScoped {
        val len = alloc<size_tVar>()
        lxb_html_document_title(checkDocument(), len.ptr)
            ?.toKStringFromUtf8(len.value)
            .orEmpty()
    }

    public actual fun body(): KtSoupElement? {
        return lxb_html_document_body_element(checkDocument())
            ?.let { KtSoupElement(it.reinterpret()) }
    }

    public actual fun head(): KtSoupElement? {
        return lxb_html_document_head_element(checkDocument())
            ?.let { KtSoupElement(it.reinterpret()) }
    }

    public actual fun querySelector(selector: String): KtSoupElement? =
        querySelectorAll(checkDocument().reinterpret(), selector, single = true).firstOrNull()

    public actual fun querySelectorAll(selector: String): List<KtSoupElement> =
        querySelectorAll(checkDocument().reinterpret(), selector, single = false)

    public actual fun getElementById(id: String): KtSoupElement? = memScoped {
        val idQuery = id.cstr
        val attrQuery = "id".cstr
        val collection = checkNotNull(lxb_dom_collection_make(checkDocument().reinterpret(), START_LIST_SIZE)) {
            "Failed to create dom collection: lxb_dom_collection_make()"
        }
        defer { lxb_dom_collection_destroy(collection, self_destroy = true) }
        val status = lxb_dom_elements_by_attr(
            checkDocument().pointed.body?.reinterpret(),
            collection,
            attrQuery.ptr.reinterpret(),
            2uL,
            idQuery.ptr.reinterpret(),
            id.length.convert(),
            case_insensitive = true,
        )
        check(status == LXB_STATUS_OK) {
            "Failed lxb_dom_elements_by_attr for id='$id'"
        }

        return if (lxb_dom_collection_length(collection) > 0u) {
            lxb_dom_collection_element(collection, 0u)
                ?.let { KtSoupElement(it.reinterpret()) }
        } else {
            null
        }
    }

    public actual fun getElementsByClass(className: String): List<KtSoupElement> = memScoped {
        val body = checkDocument().pointed.body ?: return emptyList()
        val classQuery = className.cstr
        val collection = lxb_dom_collection_make(checkDocument().reinterpret(), 128u)
            ?: throw RuntimeException("Could not create collection")
        defer { lxb_dom_collection_destroy(collection, self_destroy = true) }
        val status = lxb_dom_elements_by_class_name(
            body.reinterpret(),
            collection,
            classQuery.ptr.reinterpret(),
            className.length.convert(),
        )
        check(status == LXB_STATUS_OK) {
            "Failed lxb_dom_elements_by_class_name for '$className'"
        }
        return generateElementsList(collection)
    }

    public actual fun getElementsByTagName(tagName: String): List<KtSoupElement> = memScoped {
        val body = checkDocument().pointed.body ?: return emptyList()
        val tagQuery = tagName.cstr
        val collection = lxb_dom_collection_make(checkDocument().reinterpret(), START_LIST_SIZE)
            ?: throw RuntimeException("Could not create collection")
        defer { lxb_dom_collection_destroy(collection, self_destroy = true) }
        val status = lxb_dom_elements_by_tag_name(
            body.reinterpret(),
            collection,
            tagQuery.ptr.reinterpret(),
            tagName.length.convert(),
        )
        check(status == LXB_STATUS_OK) {
            "Failed lxb_dom_elements_by_tag_name for '$tagName'"
        }

        generateElementsList(collection)
    }

    private fun generateElementsList(collection: CPointer<lxb_dom_collection_t>): List<KtSoupElement> {
        return List(lxb_dom_collection_length(collection).convert()) { i ->
            val element = checkNotNull(lxb_dom_collection_node(collection, i.convert()))
            KtSoupElement(element.reinterpret())
        }
    }

    public actual inline fun <R> use(crossinline block: (KtSoupDocument) -> R): R {
        return try {
            block(this)
        } finally {
            close()
        }
    }

    private fun checkDocument(): CPointer<lxb_html_document_t> {
        return checkNotNull(documentPointer) { ERROR_CALL_PARSE_FIRST }
    }
}
