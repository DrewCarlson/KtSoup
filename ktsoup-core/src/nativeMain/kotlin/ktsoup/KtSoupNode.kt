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

import kotlinx.cinterop.*
import lexbor.*
import lexbor.lxb_dom_node_t
import platform.posix.size_tVar

public actual open class KtSoupNode(
    private val nodePtr: CPointer<lxb_dom_node_t>,
) {
    public actual fun nodeType(): KtSoupNodeType = when (nodePtr.pointed.type) {
        LXB_DOM_NODE_TYPE_ENTITY -> KtSoupNodeType.ENTITY
        LXB_DOM_NODE_TYPE_ENTITY_REFERENCE -> KtSoupNodeType.ENTITY_REFERENCE
        LXB_DOM_NODE_TYPE_ATTRIBUTE -> KtSoupNodeType.ATTRIBUTE
        LXB_DOM_NODE_TYPE_DOCUMENT_TYPE -> KtSoupNodeType.DOCUMENT_TYPE
        LXB_DOM_NODE_TYPE_DOCUMENT -> KtSoupNodeType.DOCUMENT
        LXB_DOM_NODE_TYPE_CDATA_SECTION -> KtSoupNodeType.CDATA_SECTION
        LXB_DOM_NODE_TYPE_COMMENT -> KtSoupNodeType.COMMENT
        LXB_DOM_NODE_TYPE_DOCUMENT_FRAGMENT -> KtSoupNodeType.DOCUMENT_FRAGMENT
        LXB_DOM_NODE_TYPE_ELEMENT -> KtSoupNodeType.ELEMENT
        LXB_DOM_NODE_TYPE_LAST_ENTRY -> KtSoupNodeType.LAST_ENTRY
        LXB_DOM_NODE_TYPE_NOTATION -> KtSoupNodeType.NOTATION
        LXB_DOM_NODE_TYPE_PROCESSING_INSTRUCTION -> KtSoupNodeType.PROCESSING_INSTRUCTION
        LXB_DOM_NODE_TYPE_TEXT -> KtSoupNodeType.TEXT
        LXB_DOM_NODE_TYPE_UNDEF -> KtSoupNodeType.UNDEF
        else -> KtSoupNodeType.UNDEF
    }

    public actual fun nodeName(): String = memScoped {
        val len = alloc<size_tVar>()
        lxb_dom_node_name(nodePtr, len.ptr)
            ?.toKStringFromUtf8(len.value)
            .orEmpty()
    }

    public actual fun textContent(): String = memScoped {
        val len = alloc<size_tVar>()
        lxb_dom_node_text_content(nodePtr, len.ptr)
            ?.toKStringFromUtf8(len.value)
            .orEmpty()
    }

    public actual fun html(): String = memScoped {
        val str = lexbor_str_create()
        defer { lexbor_str_destroy(str, nodePtr.pointed.owner_document?.pointed?.text, true) }
        lxb_html_serialize_tree_str(nodePtr, str)
        str?.pointed?.data?.toKStringFromUtf8(str.pointed.length).orEmpty()
    }

    public actual fun child(index: Int): KtSoupNode? {
        var childNode = nodePtr.pointed.first_child
        var i = 0
        while (childNode != null && i < index) {
            childNode = childNode.pointed.next
            i++
        }
        return childNode?.wrap()
    }

    public actual fun children(): List<KtSoupNode> {
        val children = mutableListOf<KtSoupNode>()
        var childNode = nodePtr.pointed.first_child
        while (childNode != null) {
            children.add(childNode.wrap())
            childNode = childNode.pointed.next
        }
        return children
    }

    public actual fun parent(): KtSoupNode? {
        return nodePtr.pointed.parent?.wrap()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is KtSoupNode) return false
        return nodePtr == other.nodePtr
    }

    override fun hashCode(): Int {
        return nodePtr.hashCode()
    }

    override fun toString(): String {
        return nodeName()
    }
}
