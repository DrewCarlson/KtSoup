# Download
[![Maven Central](https://img.shields.io/maven-central/v/org.drewcarlson/ktsoup-core-jvm?label=maven&color=blue)](https://search.maven.org/search?q=g:org.drewcarlson%20a:ktsoup-*)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/org.drewcarlson/ktsoup-core-jvm?server=https%3A%2F%2Fs01.oss.sonatype.org)

![](https://img.shields.io/static/v1?label=&message=Platforms&color=grey)
![](https://img.shields.io/static/v1?label=&message=Js&color=blue)
![](https://img.shields.io/static/v1?label=&message=Jvm&color=blue)
![](https://img.shields.io/static/v1?label=&message=Linux&color=blue)
![](https://img.shields.io/static/v1?label=&message=macOS&color=blue)
![](https://img.shields.io/static/v1?label=&message=Windows&color=blue)
![](https://img.shields.io/static/v1?label=&message=iOS&color=blue)

### Repository

Releases are published to Maven Central and snapshots are published to Sonatype OSS.
Make sure the required repository is in your build script:

```kotlin
repositories {
    mavenCentral()
    // Or snapshots
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}
```


### Kotlin Gradle Script

```kotlin
dependencies {
    implementation("org.drewcarlson:ktsoup-core:{{ version }}")
    implementation("org.drewcarlson:ktsoup-ktor:{{ version }}")
}
```

### Toml

```toml
[versions]
ktsoup = "{{ version }}"

[libraries]
ktsoup-core = { module = "org.drewcarlson:ktsoup-core", version.ref = "ktsoup" }
ktsoup-ktor = { module = "org.drewcarlson:ktsoup-ktor", version.ref = "ktsoup" }
```

