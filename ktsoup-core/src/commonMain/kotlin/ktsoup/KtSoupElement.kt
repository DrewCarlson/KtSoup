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

/**
 * A DOM element from a [KtSoupDocument].
 */
public expect open class KtSoupElement : KtSoupNode {

    /**
     * Get the `id` attribute value or null if there is no `id` attribute.
     *
     * @return The `id` attribute value or null.
     */
    public fun id(): String?

    /**
     * Get the `class` attribute value or null if there is no `class` attribute.
     *
     * @return The `class` attribute value or null.
     */
    public fun className(): String?

    /**
     * Get the tag name of this element.
     *
     * @return The tag name.
     */
    public fun tagName(): String

    /**
     * Get the [name] attribute value or null if the attribute is missing.
     *
     * @return The [name] attribute value or null.
     */
    public fun attr(name: String): String?

    /**
     * Get all attributes for this element.
     *
     * @return A map of attribute keys to their value.
     */
    public fun attrs(): Map<String, String>

    /**
     * Get the first [KtSoupElement] matching the css [selector] or null
     * if there are no matches.
     *
     * @return The matching [KtSoupElement] or null.
     */
    public fun querySelector(selector: String): KtSoupElement?

    /**
     * Get all [KtSoupElement]s matching the css [selector].
     *
     * @return A list of elements matching the [selector].
     */
    public fun querySelectorAll(selector: String): List<KtSoupElement>
}
