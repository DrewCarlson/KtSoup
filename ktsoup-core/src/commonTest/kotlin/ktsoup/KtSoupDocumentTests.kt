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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.*

class KtSoupDocumentTests {

    @Test
    fun testDocument_ParseAndClose() {
        val document = KtSoupParser.parse(SIMPLE_DOCUMENT)
        document.close()
        assertFailsWith<IllegalStateException> {
            document.title()
        }
    }

    @Test
    fun testDocument_ParseChunked() {
        val chunks = SIMPLE_DOCUMENT.chunked(10)
        var currentChunk = 0
        val document = KtSoupParser.parseChunked { buffer ->
            if (currentChunk > chunks.lastIndex) {
                -1
            } else {
                chunks[currentChunk].encodeToByteArray().let { chunk ->
                    currentChunk++
                    chunk.copyInto(buffer)
                    chunk.size
                }
            }
        }
        assertEquals("Test Title", document.title())
        document.close()
    }

    @Test
    fun testDocument_ParseChunkedAsync() = runTest {
        val chunks = SIMPLE_DOCUMENT.chunked(10)
        var currentChunk = 0
        val document = KtSoupParser.parseChunkedAsync { buffer ->
            if (currentChunk > chunks.lastIndex) {
                -1
            } else {
                withContext(Dispatchers.Default) {
                    delay(1)
                    chunks[currentChunk].encodeToByteArray().let { chunk ->
                        currentChunk++
                        chunk.copyInto(buffer)
                        chunk.size
                    }
                }
            }
        }
        assertEquals("Test Title", document.title())
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
        KtSoupParser.parse(html).use(testBody)
    }
}
