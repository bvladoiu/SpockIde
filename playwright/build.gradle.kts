// :klient-js/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "spock.lair"
version = "1.0"

kotlin {
    jvm()
    js(IR) {
        browser {
            webpackTask {
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.html)
            }
        }
        val jsMain by getting {
            dependencies {
                api(libs.ktor.client.core)
                api(libs.ktor.client.js)
                api(libs.ktor.client.websockets)
            }
        }
        val jvmMain by getting {
            dependencies {
                api(libs.ktor.server.core)
                api(libs.ktor.server.cio)
                api(libs.ktor.server.websockets)
                api(libs.microsoft.playwright)
                api(libs.slf4j.api)
                api(libs.logback.classic)
            }
        }
    }
}