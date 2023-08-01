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

internal const val SIMPLE_DOCUMENT = """
<html>
<head>
<title>Test Title</title>
</head>
<body>
<div id="test-id" class="test-class">Hello World <a href="#">Link</a></div>
</body>
</html>
"""

internal const val MULTI_ELEMENT_DOCUMENT = """
<html>
<head>
<title>Test Title</title>
</head>
<body>
<div id="test-id" class="test-class">Hello World <a href="#">Link</a></div>
<div id="test-id2" class="test-class">Hello World <a href="#">Link</a></div>
<div id="test-id3" class="test-class">Hello World <a href="#">Link</a></div>
<div id="test-id4" class="test-class">Hello World <a href="#">Link</a></div>
</body>
</html>
"""
