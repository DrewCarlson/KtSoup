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
import lexbor.LXB_DOM_NODE_TYPE_ELEMENT
import lexbor.LXB_DOM_NODE_TYPE_TEXT
import platform.posix.size_t

internal fun CPointer<UByteVar>.toKStringFromUtf8(len: size_t): String {
    return readBytes(len.toInt()).decodeToString()
}

internal fun CPointer<lexbor.lxb_dom_node_t>.wrap(): KtSoupNode {
    return when (pointed.type) {
        LXB_DOM_NODE_TYPE_ELEMENT -> KtSoupElement(reinterpret())
        LXB_DOM_NODE_TYPE_TEXT -> KtSoupText(reinterpret())
        else -> KtSoupNode(reinterpret())
    }
}
