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

import kotlinx.io.files.Path
import kotlinx.io.files.source

/**
 * Parse the HTML document at [pathString] or throw if it does not exist.
 *
 * @param pathString The file path as a string.
 * @param bufferSize An optional size for the buffer to use while reading the file, default is 1024.
 * @throws kotlinx.io.IOException If the file does not exist or is unreadable.
 * @return The parsed document as a [KtSoupDocument].
 */
public fun KtSoupParser.parseFile(
    pathString: String,
    bufferSize: Int = DEFAULT_PARSE_BUFFER_SIZE,
): KtSoupDocument {
    return parseFile(Path(pathString), bufferSize)
}

/**
 * Parse the HTML document at [path] or throw if it does not exist.
 *
 * @param path The file path as a kotlinx-io [Path].
 * @param bufferSize An optional size for the buffer to use while reading the file, default is 1024.
 * @throws kotlinx.io.IOException If the file does not exist or is unreadable.
 * @return The parsed document as a [KtSoupDocument].
 */
@OptIn(ExperimentalStdlibApi::class)
public fun KtSoupParser.parseFile(
    path: Path,
    bufferSize: Int = DEFAULT_PARSE_BUFFER_SIZE,
): KtSoupDocument {
    return path.source().use { source ->
        parseChunked { buffer ->
            source.readAtMostTo(buffer, endIndex = bufferSize)
        }
    }
}
