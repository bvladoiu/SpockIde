//:android:build.gradle.kts
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

group = "spock.lair"
version = "1.0"

kotlin {
    jvm()
    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.components.resources)
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":composables"))
                implementation(libs.androidx.collection)
                implementation(compose.desktop.currentOs)

            }
        }
    }
}

compose.desktop.application {
    mainClass = "spock.lair.MainKt"
    nativeDistributions {
        targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        packageName = "spock.lair"
        packageVersion = "1.0.0"
    }
}