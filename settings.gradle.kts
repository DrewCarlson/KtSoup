pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "KtSoup"

include(":ktsoup-core", ":ktsoup-fs", ":ktsoup-ktor")