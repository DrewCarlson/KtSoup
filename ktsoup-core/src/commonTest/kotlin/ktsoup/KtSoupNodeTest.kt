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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class KtSoupNodeTest {

    @Test
    fun test() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.querySelector("#test-id")
        assertNotNull(div)
        assertEquals(KtSoupNodeType.ELEMENT, div.nodeType())
        assertEquals("DIV", div.nodeName())
        assertEquals(mapOf("class" to "test-class", "id" to "test-id"), div.attrs())
        assertEquals("test-id", div.id())
        assertEquals("test-class", div.className())
        assertEquals("Hello World Link", div.textContent())
        assertEquals(
            "<div id=\"test-id\" class=\"test-class\">Hello World <a href=\"#\">Link</a></div>",
            // NOTE: Jsoup serialization is inaccurate, manually correct issues for the sake of test consistency.
            div.html()
                .replace("\n", "")
                .replace("> H", ">H"),
        )

        val children = div.children()
        assertEquals(2, children.size)
        assertEquals(KtSoupNodeType.TEXT, children.first().nodeType())
        assertIs<KtSoupText>(children.first())
        assertEquals(KtSoupNodeType.ELEMENT, children.last().nodeType())
        assertIs<KtSoupElement>(children.last())
    }

    @Test
    fun testTextNode() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.querySelector("#test-id")
        assertNotNull(div)
        val children = div.children()
        val textNode = children.first()
        assertIs<KtSoupText>(textNode)
        assertEquals("Hello World ", textNode.textContent())
    }

    @Test
    fun testParent() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.querySelector("#test-id")
        val body = document.body()
        assertNotNull(div)
        assertEquals(body, div.parent())
    }

    @Test
    fun testChild() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.querySelector("#test-id")
        assertNotNull(div)
        val element = assertNotNull(div.child(1))
        assertEquals("A", element.nodeName())
    }

    @Test
    fun testAttr() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.querySelector("#test-id")
        assertNotNull(div)
        assertEquals("test-class", div.attr("class"))
    }

    private fun withDocument(html: String, testBody: (document: KtSoupDocument) -> Unit) {
        KtSoupParser.parse(html).use(testBody)
    }
}
