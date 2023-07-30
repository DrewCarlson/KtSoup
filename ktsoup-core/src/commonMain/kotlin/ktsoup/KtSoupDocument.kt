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

@OptIn(ExperimentalStdlibApi::class)
public expect class KtSoupDocument() : AutoCloseable {

    public fun parse(html: String): Boolean
    public fun title(): String
    public fun body(): KtSoupElement?
    public fun head(): KtSoupElement?
    public fun getElementById(id: String): KtSoupElement?
    public fun getElementsByClass(className: String): List<KtSoupElement>
    public fun getElementsByTagName(tagName: String): List<KtSoupElement>
    override fun close()
}
