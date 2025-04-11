plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "spock.lair.browser"
version = "0.1"


kotlin {
    jvm()
    js(IR){
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core)
            }
        }
        val jvmMain by getting {
            dependencies {
                api(libs.logback.classic)

                api(libs.microsoft.playwright)

                api(libs.ktor.server.core)
                api(libs.ktor.server.websockets)
                api(libs.ktor.server.call.logging)
                api(libs.ktor.server.cio)
                api(libs.ktor.client.core)
                api(libs.ktor.client.cio)
                api(libs.ktor.client.websockets)

            }
        }
        val jsMain by getting {
            dependencies {
                api(libs.ktor.client.core)
                api(libs.ktor.client.js)
                api(libs.ktor.client.websockets)
            }
        }
    }
}