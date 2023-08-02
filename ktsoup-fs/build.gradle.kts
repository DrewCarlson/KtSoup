import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.mavenPublish)
}

val hostOs = DefaultNativePlatform.getCurrentOperatingSystem()
val testGenSrcPath = "build/ktgen/config"
val installTestConfig by tasks.creating {
    val configFile = file("${testGenSrcPath}/config.kt")
    onlyIf { !configFile.exists() }
    doFirst {
        file(testGenSrcPath).mkdirs()
        if (!configFile.exists()) {
            configFile.writeText(
                """|package ktsoup
                   |
                   |const val RESOURCE_BASE_PATH = "${project.file("src/commonTest/resources").absolutePath}${File.separator}"
                   |""".trimMargin().trimMargin().replace("\\", "\\\\")
            )
        }
    }
}

kotlin {
    jvm {
        jvmToolchain(11)
    }
    js(IR) {
        nodejs()
    }
    if (!OperatingSystem.current().isLinux) mingwX64()
    if (!OperatingSystem.current().isWindows) linuxX64()
    if (!OperatingSystem.current().isWindows) linuxArm64()
    if (OperatingSystem.current().isMacOsX) macosX64()
    if (OperatingSystem.current().isMacOsX) macosArm64()
    if (OperatingSystem.current().isMacOsX) iosSimulatorArm64()
    if (OperatingSystem.current().isMacOsX) iosArm64()
    if (OperatingSystem.current().isMacOsX) iosX64()

    targets.forEach { target ->
        target.compilations.findByName("test")?.apply {
            tasks.named(compileKotlinTaskName) {
                dependsOn(installTestConfig)
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        all { explicitApi() }
        val commonMain by getting {
            dependencies {
                implementation(project(":ktsoup-core"))
                implementation(libs.kotlinx.io.core)
            }
        }
        val commonTest by getting {
            kotlin.srcDir(testGenSrcPath)
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val nativeTest by creating {
            dependsOn(commonTest)
        }

        if (!OperatingSystem.current().isLinux) {
            val mingwX64Main by getting { dependsOn(nativeMain) }
            val mingwX64Test by getting { dependsOn(nativeTest) }
        }
        if (OperatingSystem.current().isMacOsX) {
            val macosArm64Main by getting { dependsOn(nativeMain) }
            val macosArm64Test by getting { dependsOn(nativeTest) }
            val macosX64Main by getting { dependsOn(nativeMain) }
            val macosX64Test by getting { dependsOn(nativeTest) }
            val iosSimulatorArm64Main by getting { dependsOn(nativeMain) }
            val iosSimulatorArm64Test by getting { dependsOn(nativeTest) }
            val iosArm64Main by getting { dependsOn(nativeMain) }
            val iosArm64Test by getting { dependsOn(nativeTest) }
            val iosX64Main by getting { dependsOn(nativeMain) }
            val iosX64Test by getting { dependsOn(nativeTest) }
        }
        if (!OperatingSystem.current().isWindows) {
            val linuxX64Main by getting { dependsOn(nativeMain) }
            val linuxX64Test by getting { dependsOn(nativeTest) }
            val linuxArm64Main by getting { dependsOn(nativeMain) }
            val linuxArm64Test by getting { dependsOn(nativeTest) }
        }
    }
}

// A patch to satisfy Gradle's horrible behavior
tasks.getByName("dokkaHtml").dependsOn(":ktsoup-core:transformNativeMainCInteropDependenciesMetadataForIde")
tasks.getByName("dokkaHtml").dependsOn(":ktsoup-fs:transformNativeMainCInteropDependenciesMetadataForIde")