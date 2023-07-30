import java.io.File

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

val lexborBasePath = rootProject.file("lexbor/source/lexbor").absolutePath

fun lexborSourceFiles(folder: String): List<File> =
    rootProject.file("${lexborBasePath}/${folder}")
        .listFiles()
        .orEmpty()
        .toList()

fun List<File>.filterHeaders(): List<File> = filter { it.name.endsWith(".h") }

kotlin {
    /*jvm {
        jvmToolchain(8)
        withJava()
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    macosArm64()
    macosX64()
    linuxX64()
    linuxArm64()*/
    mingwX64 {
        compilations.getByName("main") {
            binaries {
                getTest("debug").apply {
                    linkerOpts("-LC:\\Users\\drewc\\Downloads", "-llexbor")
                }
            }
            val lexbor by cinterops.creating {
                packageName("lexbor")
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
                includeDirs("$lexborBasePath/..")
            }
        }
    }


    sourceSets {
        all {
            explicitApi()
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        //val jvmMain by getting
        //val jvmTest by getting

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val nativeTest by creating {
            dependsOn(commonTest)
        }

        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
        val mingwX64Test by getting {
            dependsOn(nativeTest)
        }
    }
}
