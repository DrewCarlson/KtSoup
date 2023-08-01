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
 * The primary entrypoint for KtSoup, use [parse] to receive a [KtSoupDocument]
 * to access and manipulate the provided HTML document.
 */
public expect object KtSoupParser {
    /**
     * Parse the given [html] document.
     *
     * @return The parsed document as a [KtSoupDocument].
     */
    public fun parse(html: String): KtSoupDocument
}
