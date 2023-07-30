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

import org.jsoup.nodes.Comment
import org.jsoup.nodes.Document
import org.jsoup.nodes.DocumentType
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.lang.IndexOutOfBoundsException

public actual open class KtSoupNode(
    private val node: Node,
) {
    public actual fun nodeType(): KtSoupNodeType {
        return when (node) {
            is Document -> KtSoupNodeType.DOCUMENT
            is DocumentType -> KtSoupNodeType.DOCUMENT_TYPE
            is Element -> KtSoupNodeType.ELEMENT
            is TextNode -> KtSoupNodeType.TEXT
            is Comment -> KtSoupNodeType.COMMENT
            else -> KtSoupNodeType.UNDEF
        }
    }

    public actual fun nodeName(): String {
        return node.nodeName().uppercase()
    }

    public actual fun textContent(): String {
        return (node as? Element)?.text()
            ?: (node as? TextNode)?.text()
            ?: ""
    }

    public actual fun html(): String {
        return node.outerHtml()
    }

    public actual fun child(index: Int): KtSoupNode? {
        return try {
            node.childNode(index)
        } catch (e: IndexOutOfBoundsException) {
            null
        }?.wrap()
    }

    public actual fun children(): List<KtSoupNode> {
        return node.childNodes().map { it.wrap() }
    }

    public actual fun parent(): KtSoupNode? {
        return node.parentNode()?.wrap()
    }
}
