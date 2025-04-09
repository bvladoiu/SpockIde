import org.jetbrains.compose.ExperimentalComposeLibrary


plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
}

group = "spock.lair"
version = "0.1"

kotlin {
    jvmToolchain(17)

    jvm {}
    js(IR) {
        browser{}
        binaries.executable()
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

                api(compose.runtime)
                api(compose.runtimeSaveable)

                api(compose.html.core)
                api(libs.kotlinx.datetime)
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

        val jsMain by getting {
            dependencies {
                api(compose.html.svg)
            }
        }

       /* val wasmJsMain by getting {
            dependencies {}
        }*/
    }
}

// === Implementation Notes Reminder ===
// - A11y: Use Modifier.semantics {}. Test with screen readers.
// - Animations: Use standard compose.animation APIs.
// - Variable Fonts (Roboto Flex): Place font in common-res, load with Font(... variationSettings = ...).
// - Rich Text/Code Editor: Complex. Start with BasicTextField + AnnotatedString or evaluate heavier options if needed.
// - Check library versions and KMP compatibility in your libs.versions.toml.
