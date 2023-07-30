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
import ktsoup.nodehtmlparser.Node
import ktsoup.nodehtmlparser.TextNode

// https://github.com/taoqf/node-html-parser/blob/a439a96d3b7e934f13e5795f5d16ad4a2a10da3c/src/nodes/type.ts
internal fun Node.wrap(): KtSoupNode {
    return when (nodeType) {
        1 -> KtSoupElement(this.unsafeCast<HTMLElement>())
        3 -> KtSoupText(this.unsafeCast<TextNode>())
        // 8 -> KtSoupComment(this)
        else -> KtSoupElement(this.unsafeCast<HTMLElement>())
    }
}
