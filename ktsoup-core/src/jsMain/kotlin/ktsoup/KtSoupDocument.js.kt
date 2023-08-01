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

public actual class KtSoupDocument internal constructor(
    private var document: HTMLElement?,
) : KtSoupElement(document!!) {

    public actual fun title(): String {
        return getElementsByTagName("title").firstOrNull()?.textContent().orEmpty()
    }

    public actual fun body(): KtSoupElement? {
        return getElementsByTagName("body").firstOrNull()
    }

    public actual fun head(): KtSoupElement? {
        return getElementsByTagName("head").firstOrNull()
    }

    public actual fun getElementById(id: String): KtSoupElement? {
        return checkDocument().getElementById(id)?.let { KtSoupElement(it) }
    }

    public actual fun getElementsByClass(className: String): List<KtSoupElement> {
        return querySelectorAll(".$className")
    }

    public actual fun getElementsByTagName(tagName: String): List<KtSoupElement> {
        return checkDocument().getElementsByTagName(tagName).map { KtSoupElement(it) }.toList()
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

    private fun checkDocument(): HTMLElement {
        return checkNotNull(document) { ERROR_DOCUMENT_CLOSED }
    }
}
