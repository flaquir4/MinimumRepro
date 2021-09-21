import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }
    val xcf = XCFramework()

    iosTarget("ios") {
        compilations.getByName("main") {
            cinterops.create("Mixpanel") {
                compilerOpts(
                    "-framework",
                    "Mixpanel",
                    "-F/$rootDir/Mixpanel/Release-iphonesimulator/"
                )
                includeDirs {
                    allHeaders("$rootDir/Mixpanel/Mixpanel")
                }

            }
        }
        binaries.all {
            linkerOpts(
                "-framework",
                "Mixpanel",
                "-F/$rootDir/Mixpanel/Release-iphonesimulator/"
            )
        }
        binaries.framework {
            xcf.add(this)
            embedBitcode =
                org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE
            this.buildType
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)
    }
}