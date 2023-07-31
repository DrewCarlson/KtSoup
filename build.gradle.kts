import com.diffplug.gradle.spotless.SpotlessApply
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.binaryCompat) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.downloadPlugin) apply false
}

version = System.getenv("GITHUB_REF")?.substringAfter("refs/tags/v", version.toString()) ?: version

allprojects {
    plugins.withType<NodeJsRootPlugin> {
        the<YarnRootExtension>().lockFileDirectory = rootDir.resolve("gradle/kotlin-js-store")
    }
    repositories {
        mavenCentral()
    }
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

    apply(plugin = "org.jmailen.kotlinter")
    configure<org.jmailen.gradle.kotlinter.KotlinterExtension> {
    }

    tasks.withType<SpotlessApply> {
        dependsOn("formatKotlin")
    }

    apply(plugin = "org.jetbrains.dokka")
}
