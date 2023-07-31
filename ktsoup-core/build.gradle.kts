import de.undercouch.gradle.tasks.download.Download
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.File

plugins {
    kotlin("multiplatform")
    id("de.undercouch.download")
    alias(libs.plugins.mavenPublish)
}

val lexborSourcePath = rootProject.file("lexbor/source/lexbor").absolutePath
val lexborLibBasePath = rootProject.file("lexbor/install").absolutePath

fun lexborSourceFiles(folder: String): List<File> =
    rootProject.file("${lexborSourcePath}/${folder}")
        .listFiles()
        .orEmpty()
        .toList()

fun List<File>.filterHeaders(): List<File> = filter { it.name.endsWith(".h") }

kotlin {
    jvm {
        jvmToolchain(11)
    }
    js(IR) {
        browser {
            testTask(Action {
                useKarma {
                    useFirefoxHeadless()
                }
            })
        }
        nodejs()
    }
    configure(
        listOfNotNull(
            mingwX64(),
            if (!OperatingSystem.current().isWindows) linuxX64() else null,
            if (!OperatingSystem.current().isWindows) linuxArm64() else null,
            if (OperatingSystem.current().isMacOsX) macosX64() else null,
            if (OperatingSystem.current().isMacOsX) macosArm64() else null,
            if (OperatingSystem.current().isMacOsX) iosSimulatorArm64() else null,
            if (OperatingSystem.current().isMacOsX) iosArm64() else null,
            if (OperatingSystem.current().isMacOsX) iosX64() else null,
        )
    ) {
        compilations.getByName("main") {
            val nativeTargetName = konanTarget.name.replace("_simulator", "")
            val staticLibPath = rootProject.file("lexbor-bin/${nativeTargetName}")
            val lexbor by cinterops.creating {
                packageName("lexbor")
                includeDirs("$lexborSourcePath/..")
                headers(lexborSourceFiles("core").filterHeaders())
                headers(lexborSourceFiles("css").filterHeaders())
                headers(lexborSourceFiles("dom").filterHeaders())
                headers(lexborSourceFiles("encoding").filterHeaders())
                headers(lexborSourceFiles("html").filterHeaders())
                headers(lexborSourceFiles("ns").filterHeaders())
                headers(lexborSourceFiles("ports").filterHeaders())
                headers(lexborSourceFiles("punycode").filterHeaders())
                headers(lexborSourceFiles("selectors").filterHeaders())
                headers(lexborSourceFiles("tag").filterHeaders())
                //headers(lexborSourceFiles("unicode").filterHeaders())
                headers(lexborSourceFiles("utils").filterHeaders())
                // NOTE: Under normal conditions, lexbor compiles with __declspec(dllimport)
                // for static win32 builds.  These compilerOpts disable this behavior for
                // correct static linking on windows.
                compilerOpts("-DLEXBOR_BUILDING", "-DLEXBOR_STATIC")
                fun applyExtraOpts() {
                    extraOpts(
                        "-libraryPath", staticLibPath.absolutePath,
                        "-staticLibrary", "liblexbor_static.a",
                        "-compiler-options", "-std=c99"
                    )
                }
                when (konanTarget) {
                    KonanTarget.IOS_ARM64,
                    KonanTarget.IOS_SIMULATOR_ARM64,
                    KonanTarget.IOS_X64,
                    KonanTarget.MACOS_ARM64,
                    KonanTarget.MACOS_X64 -> if (OperatingSystem.current().isMacOsX) applyExtraOpts()
                    KonanTarget.LINUX_ARM64,
                    KonanTarget.LINUX_X64 -> if (!OperatingSystem.current().isWindows) applyExtraOpts()
                    KonanTarget.MINGW_X64 -> if (OperatingSystem.current().isWindows) applyExtraOpts()
                    else -> Unit
                }
                val downloadTaskName = "downloadLiblexbor${nativeTargetName}"
                val extractTaskName = "extractLiblexbor${nativeTargetName}"
                if (tasks.none { it.name == downloadTaskName }) {
                    val downloadTask = tasks.register<Download>("downloadLiblexbor${nativeTargetName}") {
                        enabled = !staticLibPath.exists()
                        src("https://github.com/DrewCarlson/KtSoup/releases/download/lexbor-v${libs.versions.lexbor.get()}/${nativeTargetName}.zip")
                        dest(buildDir.resolve("${nativeTargetName}.zip"))
                    }
                    val extractTask = tasks.register<Copy>("extractLiblexbor${nativeTargetName}") {
                        enabled = !staticLibPath.exists()
                        dependsOn(downloadTask.get())
                        from(zipTree(downloadTask.get().dest))
                        into(rootProject.file("lexbor-bin").absolutePath)
                    }
                    tasks.getByName(interopProcessingTaskName).dependsOn(extractTask)
                } else {
                    tasks.getByName(interopProcessingTaskName).dependsOn(extractTaskName)
                }
            }
        }
    }

    sourceSets {
        all {
            explicitApi()
            languageSettings {
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.jsoup)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("node-html-parser", "6.1.5"))
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

        val mingwX64Main by getting { dependsOn(nativeMain) }
        val mingwX64Test by getting { dependsOn(nativeTest) }
        if (OperatingSystem.current().isMacOsX) {
            val macosArm64Main by getting { dependsOn(nativeMain) }
            val macosArm64Test by getting { dependsOn(nativeTest) }
            val macosX64Main by getting { dependsOn(nativeMain) }
            val macosX64Test by getting { dependsOn(nativeTest) }
            val iosSimulatorArm64Main by getting { dependsOn(nativeMain) }
            val iosSimulatorArmTest by getting { dependsOn(nativeTest) }
            val iosArm64Main by getting { dependsOn(nativeMain) }
            val iosArmTest by getting { dependsOn(nativeTest) }
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
