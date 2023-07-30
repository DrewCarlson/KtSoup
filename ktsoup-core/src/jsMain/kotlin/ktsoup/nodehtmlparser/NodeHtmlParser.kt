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
package ktsoup.nodehtmlparser

// https://github.com/taoqf/node-html-parser
@JsNonModule
@JsModule("node-html-parser")
internal external object HTMLParser {
    fun parse(html: String): HTMLElement
}

internal external interface Node {
    val nodeType: Int
    val innerText: String
    val textContent: String
}

internal external class TextNode : Node {
    val text: String
    val isWhitespace: Boolean
    override val nodeType: Int
    override val innerText: String
    override val textContent: String
}

internal external class CommentNode : Node {
    override val nodeType: Int
    override val innerText: String
    override val textContent: String
}

internal external class HTMLElement : Node {
    override val nodeType: Int
    override val innerText: String
    override val textContent: String

    val tagName: String
    val outerHTML: String

    val parentNode: Node
    val childNodes: Array<Node>

    fun getElementById(id: String): HTMLElement
    fun getElementsByTagName(tagName: String): Array<HTMLElement>
    fun querySelectorAll(selector: String): Array<Node>
    fun querySelector(selector: String): Node

    fun getAttribute(key: String): String

    val attributes: dynamic
}
