plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "spock.lair.web"
version = "1.0.0"

kotlin {
    jvm()
    js(IR) {
        browser {
            binaries.executable()
        }
    }

    sourceSets {

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-browser-js:2025.4.13")
            }
        }
    }
}