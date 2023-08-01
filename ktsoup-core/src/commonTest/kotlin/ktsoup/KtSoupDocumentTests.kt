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

private const val SIMPLE_DOCUMENT = """
<html>
<head>
<title>Test Title</title>
</head>
<body>
<div id="test-id" class="test-class">Hello World <a href="#">Link</a></div>
</body>
</html>
"""

class KtSoupDocumentTests {

    @Test
    fun test() {
        val document = KtSoupDocument()
        assertTrue(document.parse(SIMPLE_DOCUMENT))
        assertEquals("Test Title", document.title())
        assertNotNull(document.head())
        assertNotNull(document.body())

        // val div = document.getElementsByTagName("div").singleOrNull()
        // val div = document.getElementsByClass("test-class").singleOrNull()
        // val div = document.getElementById("test-id")
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

        document.close()
    }
}
