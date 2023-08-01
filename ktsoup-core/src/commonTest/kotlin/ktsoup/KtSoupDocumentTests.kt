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

import kotlin.test.*

class KtSoupDocumentTests {

    @Test
    fun testDocument_Parse() {
        val document = KtSoupDocument()
        assertTrue(document.parse(SIMPLE_DOCUMENT))
        document.close()
    }

    @Test
    fun testDocument_Head() = withDocument(SIMPLE_DOCUMENT) { document ->
        val head = assertNotNull(document.head())
        assertEquals("head", head.tagName())
    }

    @Test
    fun testDocument_Body() = withDocument(SIMPLE_DOCUMENT) { document ->
        val head = assertNotNull(document.body())
        assertEquals("body", head.tagName())
    }

    @Test
    fun testDocument_Title() = withDocument(SIMPLE_DOCUMENT) { document ->
        assertEquals("Test Title", document.title())
    }

    @Test
    fun testDocument_getElementById() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.getElementById("test-id")
        assertNotNull(div)
        assertIs<KtSoupElement>(div)
    }

    @Test
    fun testDocument_getElementsByClass() = withDocument(SIMPLE_DOCUMENT) { document ->
        val divs = document.getElementsByClass("test-class")
        assertEquals(1, divs.size)
        val div = divs.first()
        assertNotNull(div)
        assertIs<KtSoupElement>(div)
    }

    @Test
    fun testDocument_getElementsByTagName() = withDocument(SIMPLE_DOCUMENT) { document ->
        val divs = document.getElementsByTagName("div")
        assertEquals(1, divs.size)
        val div = divs.first()
        assertNotNull(div)
        assertIs<KtSoupElement>(div)
    }

    @Test
    fun testDocument_querySelector() = withDocument(SIMPLE_DOCUMENT) { document ->
        val div = document.querySelector("#test-id")
        assertNotNull(div)
        assertIs<KtSoupElement>(div)
    }

    @Test
    fun testDocument_querySelectorAll() = withDocument(MULTI_ELEMENT_DOCUMENT) { document ->
        val divs = document.querySelectorAll(".test-class")
        assertEquals(4, divs.size)
        val div1 = divs[0]
        val div2 = divs[1]
        val div3 = divs[2]
        val div4 = divs[3]
        assertEquals("div", div1.tagName())
        assertEquals("div", div2.tagName())
        assertEquals("div", div3.tagName())
        assertEquals("div", div4.tagName())
    }

    private fun withDocument(html: String, testBody: (document: KtSoupDocument) -> Unit) {
        val document = KtSoupDocument()
        assertTrue(document.parse(html))
        document.use(testBody)
    }

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
}
