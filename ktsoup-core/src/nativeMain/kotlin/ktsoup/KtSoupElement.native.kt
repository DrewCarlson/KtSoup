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

public actual open class KtSoupElement internal constructor(
    private val elementPtr: CPointer<lxb_dom_element_t>,
) : KtSoupNode(elementPtr.pointed.node.ptr) {
    public actual fun id(): String? = memScoped {
        val len = alloc<size_tVar>()
        lxb_dom_element_id(
            elementPtr.pointed.readValue(),
            len.ptr,
        )?.toKStringFromUtf8(len.value)
    }

    public actual fun className(): String? = memScoped {
        val len = alloc<size_tVar>()
        lxb_dom_element_class(
            elementPtr.pointed.readValue(),
            len.ptr,
        )?.toKStringFromUtf8(len.value)
    }

    public actual fun tagName(): String = memScoped {
        val len = alloc<size_tVar>()
        lxb_dom_element_qualified_name(
            elementPtr.pointed.readValue(),
            len.ptr,
        )?.toKStringFromUtf8(len.value)
            .orEmpty()
    }

    public actual fun attr(name: String): String? = memScoped {
        lxb_dom_element_attr_by_name(
            elementPtr.pointed.readValue(),
            name.cstr.ptr.reinterpret(),
            name.length.convert(),
        )?.let { attr ->
            val len = alloc<size_tVar>()
            lxb_dom_attr_value(attr, len.ptr)?.toKStringFromUtf8(len.value)
        }
    }

    public actual fun attrs(): Map<String, String> {
        val attrMap = mutableMapOf<String, String>()
        memScoped {
            var attr = lxb_dom_element_first_attribute(elementPtr)
            while (attr != null) {
                val len = alloc<size_tVar>()
                val attrName = lxb_dom_attr_qualified_name(attr, len.ptr)
                    ?.toKStringFromUtf8(len.value)
                val attrValue = lxb_dom_attr_value(attr, len.ptr)
                    ?.toKStringFromUtf8(len.value)
                if (attrName != null && attrValue != null) {
                    attrMap[attrName] = attrValue
                }
                attr = lxb_dom_element_next_attribute(attr)
            }
        }
        return attrMap
    }

    public actual fun querySelector(selector: String): KtSoupElement? =
        querySelectorAll(elementPtr.reinterpret(), selector, single = true).firstOrNull()

    public actual fun querySelectorAll(selector: String): List<KtSoupElement> =
        querySelectorAll(elementPtr.reinterpret(), selector, single = false)
}
