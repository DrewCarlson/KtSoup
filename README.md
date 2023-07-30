# KtSoup

A multiplatform HTML5 parsing library built on [Lexbor](https://github.com/lexbor/lexbor).

**WIP** The project is not yet published or ready for use, though it may be a helpful example.

TODO:
 - Finalize initial API surface
 - Add Lexbor build workflow
 - Add Jvm and JS targets
 - Add test workflows
 - Add documentation and publishing workflows
 - Extra: Add all-in-one fetch and parse API using Ktor

### Example

```kotlin
val documentString = """
<html>
    <body>
        <div id="test" class="test">Hello World</div>
    </body>
</html>
"""

val document = KtSoupDocument()
document.parse(documentString)
document.use { document ->
    val div = document.getElementsById("test")
    println(div.textContent()) // output: Hello World
    println(div.html())        // output: <div id="test" class="test">Hello World</div>
}

```


## Download

[![Maven Central](https://img.shields.io/maven-central/v/org.drewcarlson/ktsoup-core-jvm?label=maven&color=blue)](https://search.maven.org/search?q=g:org.drewcarlson%20a:objectstore-*)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/org.drewcarlson/ktsoup-core-jvm?server=https%3A%2F%2Fs01.oss.sonatype.org)

![](https://img.shields.io/static/v1?label=&message=Platforms&color=grey)
![](https://img.shields.io/static/v1?label=&message=Js&color=blue)
![](https://img.shields.io/static/v1?label=&message=Jvm&color=blue)
![](https://img.shields.io/static/v1?label=&message=Linux&color=blue)
![](https://img.shields.io/static/v1?label=&message=macOS&color=blue)
![](https://img.shields.io/static/v1?label=&message=Windows&color=blue)
![](https://img.shields.io/static/v1?label=&message=iOS&color=blue)
![](https://img.shields.io/static/v1?label=&message=tvOS&color=blue)
![](https://img.shields.io/static/v1?label=&message=watchOS&color=blue)

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
