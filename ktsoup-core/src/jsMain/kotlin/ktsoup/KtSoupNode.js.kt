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

import ktsoup.nodehtmlparser.HTMLElement
import ktsoup.nodehtmlparser.Node

public actual open class KtSoupNode internal constructor(
    private val node: Node,
) {
    // https://github.com/taoqf/node-html-parser/blob/a439a96d3b7e934f13e5795f5d16ad4a2a10da3c/src/nodes/type.ts
    public actual fun nodeType(): KtSoupNodeType {
        return when (node.nodeType) {
            1 -> KtSoupNodeType.ELEMENT
            3 -> KtSoupNodeType.TEXT
            8 -> KtSoupNodeType.COMMENT
            else -> KtSoupNodeType.UNDEF
        }
    }

    public actual fun nodeName(): String {
        return asHtmlElement()?.tagName?.uppercase() ?: nodeType().name
    }

    public actual fun textContent(): String {
        return node.textContent
    }

    public actual fun html(): String {
        return asHtmlElement()?.outerHTML ?: node.textContent
    }

    public actual fun child(index: Int): KtSoupNode? {
        return asHtmlElement()?.childNodes?.get(index)?.wrap()
    }

    public actual fun children(): List<KtSoupNode> {
        return asHtmlElement()?.childNodes?.map { it.wrap() }.orEmpty()
    }

    public actual fun parent(): KtSoupNode? {
        return asHtmlElement()?.parentNode?.wrap()
    }

    private fun asHtmlElement(): HTMLElement? =
        if (nodeType() == KtSoupNodeType.ELEMENT) {
            node.unsafeCast<HTMLElement>()
        } else {
            null
        }

    actual override fun toString(): String {
        return nodeName()
    }

    actual override fun hashCode(): Int {
        return node.hashCode()
    }

    actual override fun equals(other: Any?): Boolean {
        if (other !is KtSoupNode) return false
        return node == other.node
    }
}
