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

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

public actual class KtSoupDocument {

    private var document: Document? = null

    public actual fun parse(html: String): Boolean {
        document = Jsoup.parse(html)
        return true
    }

    public actual fun title(): String {
        return checkDocument().title()
    }

    public actual fun body(): KtSoupElement? {
        return KtSoupElement(checkDocument().body())
    }

    public actual fun head(): KtSoupElement? {
        return KtSoupElement(checkDocument().head())
    }

    public actual fun querySelector(selector: String): KtSoupElement? {
        return checkDocument().selectFirst(selector)?.let { KtSoupElement(it) }
    }

    public actual fun querySelectorAll(selector: String): List<KtSoupElement> {
        return checkDocument().select(selector).map { KtSoupElement(it) }
    }

    public actual fun getElementById(id: String): KtSoupElement? {
        return checkDocument().getElementById(id)?.let { KtSoupElement(it) }
    }

    public actual fun getElementsByClass(className: String): List<KtSoupElement> {
        return checkDocument().getElementsByClass(className).map { KtSoupElement(it) }
    }

    public actual fun getElementsByTagName(tagName: String): List<KtSoupElement> {
        return checkDocument().getElementsByTag(tagName).map { KtSoupElement(it) }
    }

    public actual fun close() {
        document = null
    }

    public actual inline fun <R> use(crossinline block: (KtSoupDocument) -> R): R {
        return try {
            block(this)
        } finally {
            close()
        }
    }

    private fun checkDocument(): Document {
        return checkNotNull(document) { ERROR_CALL_PARSE_FIRST }
    }
}
