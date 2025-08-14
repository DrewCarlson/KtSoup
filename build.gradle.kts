import com.diffplug.gradle.spotless.SpotlessApply
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.spotless)
    alias(libs.plugins.mavenPublish) apply false
    alias(libs.plugins.downloadPlugin) apply false
    //id("com.louiscad.complete-kotlin") version "1.1.0"
}

allprojects {
    plugins.withType<NodeJsRootPlugin> {
        the<YarnRootExtension>().lockFileDirectory = rootDir.resolve("gradle/kotlin-js-store")
    }
    repositories {
        mavenCentral()
    }
}

dependencies {
    dokka(project(":ktsoup-core"))
    dokka(project(":ktsoup-fs"))
    dokka(project(":ktsoup-ktor"))
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    configure<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension> {
    }
    rootProject.dependencies.add("kover", project(path))

    apply(plugin = "com.diffplug.spotless")
    configure<SpotlessExtension> {
        kotlin {
            target("**/**.kt")
            licenseHeaderFile(rootDir.resolve("licenseHeader.txt"))
        }
    }

    apply(plugin = "org.jetbrains.dokka")
}

System.getenv("GITHUB_REF")?.let { ref ->
    if (ref.startsWith("refs/tags/v")) {
        version = ref.substringAfterLast("refs/tags/v")
    }
}
