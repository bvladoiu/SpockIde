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
                implementation(libs.kotlin.wrappers.browser.js)
            }
        }
    }
}
