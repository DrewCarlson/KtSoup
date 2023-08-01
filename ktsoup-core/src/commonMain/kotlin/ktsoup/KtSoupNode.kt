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
 * The base type representing all DOM nodes in a [KtSoupDocument].
 */
public expect open class KtSoupNode {

    /**
     * Get the [KtSoupNodeType] of this node.
     *
     * @return The node type.
     */
    public fun nodeType(): KtSoupNodeType

    /**
     * Get the node name of this node.
     *
     * This will be the capitalized name of a [KtSoupElement]s tag name or
     * the [KtSoupNodeType]s name.
     *
     * @return The node name.
     */
    public fun nodeName(): String

    /**
     * Get the text content of this node.
     *
     * For example if this node represents the following HTML:
     * `<div>Hello <a href="#">World!</a></div>`
     * The [textContent] will be: `Hello World!`
     *
     * @return The text content within this node.
     */
    public fun textContent(): String

    /**
     * Get the outer HTML as a string or the [textContent] if it's not an Element.
     *
     * For example if this node represents an `<a>` tag, the return value
     * would be `<a href="#">My Link</a>`.
     *
     * Multiplatform Note: This value may contain differences in whitespace and
     * newlines depending on the platform, but the content is functionally identical.
     *
     * @return The outer HTML of the node or it's [textContent].
     */
    public fun html(): String

    /**
     * Get the child [KtSoupNode] at the given [index] or null if the index is
     * out of range.
     *
     * @return The child [KtSoupNode] or null if [index] is out of range.
     */
    public fun child(index: Int): KtSoupNode?

    /**
     * Get a list of all the child nodes.
     *
     * @return THe child [KtSoupNode]s.
     */
    public fun children(): List<KtSoupNode>

    /**
     * Get this node's parent [KtSoupNode] or null if it has no parent.
     *
     * *Note for Javascript:* Currently only [KtSoupElement]'s will return
     * their parent, [KtSoupText] nodes for example will return null.
     *
     * @return The parent node or null if it has no parent.
     */
    public fun parent(): KtSoupNode?

    override fun toString(): String
    override fun hashCode(): Int
    override fun equals(other: Any?): Boolean
}
