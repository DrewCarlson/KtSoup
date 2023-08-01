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
import platform.posix.size_t

internal fun CPointer<UByteVar>.toKStringFromUtf8(len: size_t): String {
    return readBytes(len.toInt()).decodeToString()
}

internal fun CPointer<lxb_dom_node_t>.wrap(): KtSoupNode {
    return when (pointed.type) {
        LXB_DOM_NODE_TYPE_ELEMENT -> KtSoupElement(reinterpret())
        LXB_DOM_NODE_TYPE_TEXT -> KtSoupText(reinterpret())
        else -> KtSoupNode(reinterpret())
    }
}

internal data class SelectorCallbackContext(
    val single: Boolean,
    val nodes: MutableList<CPointer<lxb_dom_node_t>> = mutableListOf(),
)

internal val selectorCallback = staticCFunction<
    CPointer<lxb_dom_node_t>?,
    lxb_css_selector_specificity_t,
    COpaquePointer?,
    lxb_status_t,
    > { node, _, ctxPtr ->
    val ctx = ctxPtr!!.asStableRef<SelectorCallbackContext>().get()
    ctx.nodes.add(node!!)
    if (ctx.single) {
        LXB_STATUS_ABORTED
    } else {
        LXB_STATUS_OK
    }
}

internal fun querySelectorAll(
    elementPtr: CValuesRef<lxb_dom_node_t /* = lxb_dom_node */>?,
    selector: String,
    single: Boolean,
): List<KtSoupElement> = memScoped {
    val parser = lxb_css_parser_create().also {
        lxb_css_parser_init(it, null)
        defer { lxb_css_parser_destroy(it, self_destroy = true) }
    }
    val selectors = lxb_selectors_create().also {
        lxb_selectors_init(it)
        defer { lxb_selectors_destroy(it, self_destroy = true) }
    }
    val list = lxb_css_selectors_parse(
        parser,
        selector.cstr.ptr.reinterpret(),
        selector.length.toULong(),
    ) ?: return emptyList() // failed to parse selector, maybe throw?
    defer { lxb_css_selector_list_destroy_memory(list) }

    val ctx = SelectorCallbackContext(single = single)
    val ctxRef = StableRef.create(ctx)
    lxb_selectors_find(selectors, elementPtr, list, selectorCallback, ctxRef.asCPointer())
    ctxRef.dispose()

    ctx.nodes.mapNotNull { it.wrap() as? KtSoupElement }
}
