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

import org.jsoup.nodes.Element

public actual open class KtSoupElement internal constructor(
    private val element: Element,
) : KtSoupNode(element) {
    public actual fun id(): String? {
        return element.id().takeUnless(String::isEmpty)
    }

    public actual fun className(): String? {
        return element.className().takeUnless(String::isEmpty)
    }

    public actual fun tagName(): String {
        return element.tagName()
    }

    public actual fun attr(name: String): String? {
        return element.attr(name).takeUnless(String::isEmpty)
    }

    public actual fun attrs(): Map<String, String> {
        return buildMap(element.attributesSize()) {
            element.attributes().forEach { attr ->
                put(attr.key, attr.value)
            }
        }
    }

    public actual fun querySelector(selector: String): KtSoupElement? {
        return element.selectFirst(selector)?.let { KtSoupElement(it) }
    }

    public actual fun querySelectorAll(selector: String): List<KtSoupElement> {
        return element.select(selector).map { KtSoupElement(it) }
    }
}
