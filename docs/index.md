# KtSoup

![Maven Central](https://img.shields.io/maven-central/v/org.drewcarlson/ktsoup-core-jvm?label=maven&color=blue)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/org.drewcarlson/ktsoup-core-jvm?server=https%3A%2F%2Fs01.oss.sonatype.org)
![](https://github.com/DrewCarlson/KtSoup/workflows/Tests/badge.svg)
[![codecov](https://codecov.io/gh/DrewCarlson/KtSoup/branch/main/graph/badge.svg?token=BTQ8XQOD86)](https://codecov.io/gh/DrewCarlson/KtSoup)

![](https://img.shields.io/static/v1?label=&message=Platforms&color=grey)
![](https://img.shields.io/static/v1?label=&message=Js&color=blue)
![](https://img.shields.io/static/v1?label=&message=Jvm&color=blue)
![](https://img.shields.io/static/v1?label=&message=Linux&color=blue)
![](https://img.shields.io/static/v1?label=&message=macOS&color=blue)
![](https://img.shields.io/static/v1?label=&message=Windows&color=blue)
![](https://img.shields.io/static/v1?label=&message=iOS&color=blue)

A Kotlin Multiplatform HTML5 parsing library built on [Lexbor](https://github.com/lexbor/lexbor), [Jsoup](https://jsoup.org/), and [node-html-parser](https://github.com/taoqf/node-html-parser).

**[API Docs](kdoc)**

### Checkout the [Getting Started](1-getting-started.md) guide.

```kotlin
val html = """
<html>
    <body>
        <div id="test" class="test">Hello World</div>
    </body>
</html>
"""

KtSoupParser.parse(html).use { document ->
    val div = document.querySelector("#test")
    println(div.textContent()) // Hello World
    println(div.html())        // <div id="test" class="test">Hello World</div>
    println(div.className())   // test
}
```
