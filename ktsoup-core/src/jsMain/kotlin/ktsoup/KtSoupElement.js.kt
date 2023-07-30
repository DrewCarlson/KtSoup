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

public actual class KtSoupElement internal constructor(
    private val element: HTMLElement,
) : KtSoupNode(element) {
    public actual fun id(): String? {
        return element.getAttribute("id").takeUnless(String::isEmpty)
    }

    public actual fun className(): String? {
        return element.getAttribute("class").takeUnless(String::isEmpty)
    }

    public actual fun tagName(): String {
        return element.tagName
    }

    public actual fun attr(name: String): String? {
        return element.getAttribute(name)
    }

    @Suppress("UNUSED_VARIABLE")
    public actual fun attrs(): Map<String, String> {
        // Capture element locally or name will be mangled in `js(...)`
        val e = element
        val keys = js("Object.keys(e.attributes)").unsafeCast<Array<String>>()
        return buildMap(keys.size) {
            keys.forEach { key ->
                put(key, element.getAttribute(key))
            }
        }
    }
}
