@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "spock.lair.browser"
version = "1.0.0"


kotlin {
    jvm{
        mainRun {
            mainClass.set("spock.lair.JvmMainKt")
        }
    }
    js(IR) {
        browser {
            binaries.executable()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.logback.classic)
                implementation(libs.microsoft.playwright)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.websockets)
                implementation(libs.ktor.server.call.logging)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.websockets)
                implementation(libs.native.hooks)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.js)
                implementation(libs.ktor.client.websockets)
                implementation(libs.kotlin.wrappers.browser)
            }
        }
    }
}

gradle.projectsEvaluated {
    tasks.matching { it.name == "jvmRun" }.configureEach {
        dependsOn("jsBrowserProductionWebpack")
    }
}
