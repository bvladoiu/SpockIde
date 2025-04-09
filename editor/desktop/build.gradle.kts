// editor-desktop/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

group = "spock.lair"
version = "1.0.0"

kotlin {
    jvmToolchain(17)
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":compose"))
                implementation(libs.kotlinx.datetime)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
    }
}
compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
            )
            packageName = "Spock Ide"
            packageVersion = "1.0.0"
            /*  iconFile.set(project.file("assets/launcher/app-icon.icns"))
              windows { iconFile.set(project.file("assets/launcher/app-icon.ico")) }
              linux { iconFile.set(project.file("assets/launcher/app-icon.png")) }
              macOS { iconFile.set(project.file("assets/launcher/app-icon.icns")) }*/
        }
        jvmArgs += listOf("-Xmx2G", "-Dfile.encoding=UTF-8", "-Dapple.awt.application.appearance=system")
    }
}


// === Implementation Notes Reminder ===
// - A11y: Use Modifier.semantics {}. Test with screen readers.
// - Animations: Use standard compose.animation APIs.
// - Variable Fonts (Roboto Flex): Place font in common-res, load with Font(... variationSettings = ...).
// - Rich Text/Code Editor: Complex. Start with BasicTextField + AnnotatedString or evaluate heavier options if needed.
// - Check library versions and KMP compatibility in your libs.versions.toml.
