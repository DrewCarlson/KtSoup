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

internal const val ERROR_DOCUMENT_CLOSED = "The KtSoupDocument was closed and cannot be accessed again."

/**
 * Represents a DOM document providing APIs to query and manipulate the document.
 */
public expect class KtSoupDocument : KtSoupElement {

    /**
     * Get the document's title or an empty string if no title is found.
     *
     * @return The document title or an empty string.
     */
    public fun title(): String

    /**
     * Get a [KtSoupElement] representing the document's body or null if
     * there is no body.
     *
     * @return The document body or null if no body is found.
     */
    public fun body(): KtSoupElement?

    /**
     * Get a [KtSoupElement] representing the document's head or null if
     * there is no head.
     *
     * @return The document head or null if no head is found.
     */
    public fun head(): KtSoupElement?

    /**
     * Get the first element with an `id` attribute matching [id] or null
     * if there are no matching elements.
     *
     * @return The first element with [id] or null.
     */
    public fun getElementById(id: String): KtSoupElement?

    /**
     * Get all elements with the provided [className].
     *
     * @return All elements with the [className].
     */
    public fun getElementsByClass(className: String): List<KtSoupElement>

    /**
     * Get all elements with the provided [tagName].
     *
     * @return All elements with the [tagName].
     */
    public fun getElementsByTagName(tagName: String): List<KtSoupElement>

    /**
     * Free all resources used for this document.
     */
    public fun close()

    /**
     * A convenience function to interact with the document in [block]
     * and automatically invoke [close] at the end.
     *
     * @return The [R] value returned from [block]
     */
    public inline fun <R> use(crossinline block: (KtSoupDocument) -> R): R
}
