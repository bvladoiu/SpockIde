import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
}

group = "spock.lair"
version = "0.1"

kotlin {
    jvm()
    js(IR){
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.material3)
                api(compose.animation)
                api(compose.ui)
                api(compose.foundation)
                api(compose.uiUtil)
                api(compose.animationGraphics)
                api(compose.material3AdaptiveNavigationSuite)
                api(libs.kotlinx.coroutines.core)

                api(libs.compose.colorpicker)
                api(libs.darkrockstudios.mpfilepicker)


                api(compose.html.core)
                api(compose.materialIconsExtended)
                api(compose.components.resources)
            }
        }

        @OptIn(ExperimentalComposeLibrary::class)
        val jvmMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
                api(compose.desktop.components.splitPane)
                api(compose.desktop.components.animatedImage)
                api(compose.uiTooling)
                api(compose.preview)
                api(compose.components.uiToolingPreview)
            }
        }
    }
}
