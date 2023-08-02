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

import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private const val SIMPLE_DOCUMENT_PATH = "${RESOURCE_BASE_PATH}SimpleDocument.html"
private const val LARGE_DOCUMENT_PATH = "${RESOURCE_BASE_PATH}LargeDocument.html"

class KtSoupParserFsTest {

    @Test
    fun testParse_FilePathString() {
        KtSoupParser.parseFile(SIMPLE_DOCUMENT_PATH).use { document ->
            assertNotNull(document.querySelector("#test-id"))
        }
    }

    @Test
    fun testParse_FilePath() {
        KtSoupParser.parseFile(Path(SIMPLE_DOCUMENT_PATH)).use { document ->
            assertNotNull(document.querySelector("#test-id"))
        }
    }

    @Test
    fun testParse_LargeDocument() {
        KtSoupParser.parseFile(LARGE_DOCUMENT_PATH, bufferSize = 100).use { document ->
            val p = assertNotNull(document.querySelectorAll("p").lastOrNull())
            assertTrue(p.textContent().endsWith("nulla ac erat."))
        }
    }
}
