import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

// browser/build.gradle.kts

plugins {
    alias(libs.plugins.kotlinMultiplatform)

}

group = "spock.lair.browser"
version = "0.1"


kotlin {
    jvmToolchain(17)
    jvm("webkit") {
        compilerOptions {

            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            mainRun {
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                mainClass = "spock/lair/browser/PlaywrightKt"
            }
        }
    }
    js("page", IR) {
        browser {

        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":tools"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val webkitMain by getting {
            dependencies {

            }
        }
        val pageMain by getting {
            dependencies {}
        }
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        target = "es2015"
    }
}


// --- Custom Task to Run the JVM CLI ---
// description = "Runs the browser module's JVM controller CLI"

// Set the classpath: includes compiled classes + runtime dependencies
//  val browserMainSourceSet = kotlin.sourceSets.getByName("browserMain")
// classpath = browserMainSourceSet.runtimeClasspath + browserMainSourceSet.output

// Set the main class to execute
// IMPORTANT: Use the correct Fully Qualified Name + Kt suffix
//mainClass.set("spock.lair.browser.jvm.MainKt")

// Configure Ktor server within :tools or here if needed
// Configure Playwright options in your Kotlin code (e.g., setHeadless(false))