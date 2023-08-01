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

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue

class KtSoupParserKtorTest {

    @AfterTest
    fun cleanup() {
        KtSoupParser.clearClient()
    }

    @Test
    fun testKtorParse_DefaultClient() = runTest {
        val document = KtSoupParser.parseRemote("https://duckduckgo.com")
        assertTrue(document.title().startsWith("DuckDuckGo"))
    }

    @Test
    fun testKtorParse_CustomClient() = runTest {
        KtSoupParser.setClient(HttpClient(OkHttp))
        val document = KtSoupParser.parseRemote("https://duckduckgo.com")
        assertTrue(document.title().startsWith("DuckDuckGo"))
    }

    @Test
    fun testKtorParse_ConfiguredClient() = runTest {
        KtSoupParser.configureClient {
            defaultRequest { url("https://duckduckgo.com") }
        }
        val document = KtSoupParser.parseRemote("")
        assertTrue(document.title().startsWith("DuckDuckGo"))
    }
}
