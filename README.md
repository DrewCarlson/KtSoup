# KtSoup

![Maven Central](https://img.shields.io/maven-central/v/org.drewcarlson/ktsoup-core-jvm?label=maven&color=blue)
![](https://github.com/DrewCarlson/KtSoup/workflows/Tests/badge.svg)
[![codecov](https://codecov.io/gh/DrewCarlson/KtSoup/branch/main/graph/badge.svg?token=BTQ8XQOD86)](https://codecov.io/gh/DrewCarlson/KtSoup)

A multiplatform HTML5 parsing library built on [Lexbor](https://github.com/lexbor/lexbor), [Jsoup](https://jsoup.org/), and [node-html-parser](https://github.com/taoqf/node-html-parser).

## Usage

```kotlin
val documentString = """
<html>
    <body>
        <div id="test" class="test">Hello World</div>
    </body>
</html>
"""

val document = KtSoupParser.parse(documentString)
document.use { document ->
    val div = document.getElementById("test")
    println(div.textContent()) // output: Hello World
    println(div.html())        // output: <div id="test" class="test">Hello World</div>
}

```


## Download

[![Maven Central](https://img.shields.io/maven-central/v/org.drewcarlson/ktsoup-core-jvm?label=maven&color=blue)](https://search.maven.org/search?q=g:org.drewcarlson%20a:ktsoup-*)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/org.drewcarlson/ktsoup-core-jvm?server=https%3A%2F%2Fs01.oss.sonatype.org)

![](https://img.shields.io/static/v1?label=&message=Platforms&color=grey)
![](https://img.shields.io/static/v1?label=&message=Js&color=blue)
![](https://img.shields.io/static/v1?label=&message=Jvm&color=blue)
![](https://img.shields.io/static/v1?label=&message=Linux&color=blue)
![](https://img.shields.io/static/v1?label=&message=macOS&color=blue)
![](https://img.shields.io/static/v1?label=&message=Windows&color=blue)
![](https://img.shields.io/static/v1?label=&message=iOS&color=blue)

```kotlin
repositories {
    mavenCentral()
    // Or snapshots
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.drewcarlson:ktsoup-core:$VERSION")
}
```

<details>
<summary>Toml (Click to expand)</summary>

```toml
[versions]
ktsoup = "1.0.0-SNAPSHOT"

[libraries]
ktsoup-core = { module = "org.drewcarlson:ktsoup-core", version.ref = "ktsoup" }
```
</details>


### License

This project is licensed under Apache-2.0, found in [LICENSE](LICENSE).
