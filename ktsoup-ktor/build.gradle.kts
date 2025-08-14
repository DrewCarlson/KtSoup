import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.mavenPublish)
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
    abiValidation {
        enabled = true
    }
    jvmToolchain(11)
    jvm()
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useFirefoxHeadless()
                }
            }
        }
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


    sourceSets {
        all { explicitApi() }
        val commonMain by getting {
            dependencies {
                implementation(project(":ktsoup-core"))
                implementation(libs.ktor.client.core)
                implementation(libs.atomicfu)
                implementation(libs.coroutines.core)
            }
        }
        val commonTest by getting {
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
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.okhttp)
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
