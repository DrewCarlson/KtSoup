import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.mavenPublish)
}

val hostOs = DefaultNativePlatform.getCurrentOperatingSystem()
val testGenSrcPath = "build/ktgen/config"
val installTestConfig by tasks.registering {
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
    @OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
    abiValidation {
        enabled = true
    }
    jvmToolchain(17)
    jvm()
    js(IR) {
        nodejs()
    }
    val currentOs = OperatingSystem.current()
    if (!currentOs.isLinux) mingwX64()
    if (!currentOs.isWindows) linuxX64()
    if (!currentOs.isWindows) linuxArm64()
    if (currentOs.isMacOsX) macosX64()
    if (currentOs.isMacOsX) macosArm64()
    if (currentOs.isMacOsX) iosSimulatorArm64()
    if (currentOs.isMacOsX) iosArm64()
    if (currentOs.isMacOsX) iosX64()

    targets.forEach { target ->
        target.compilations.findByName("test")?.apply {
            tasks.named(compileKotlinTaskName) {
                dependsOn(installTestConfig)
            }
        }
    }

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
    }
}
