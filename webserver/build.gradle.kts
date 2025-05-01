plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinxSerialization)
}

group = "spock.lair"
version = "1.0.0"

application {
    mainClass = "spock.lair.ApplicationKt"
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

val copyJsToResources = "copyJsToResources"
val webJsBrowserProductionWebpackTask = ":web:jsBrowserProductionWebpack"

tasks.register(copyJsToResources, DefaultTask::class) {
    group = "build"
    description = "Builds the JS bundles and copies output to webserver's resources/js directory."
    dependsOn(webJsBrowserProductionWebpackTask)

    doLast {
        val webJsDevBuildDir = rootProject.project(":web").layout.buildDirectory.get().asFile.resolve("kotlin-webpack/js/productionExecutable")
        val resourcesJsDir = project.layout.projectDirectory.dir("src/main/resources/js").asFile
        resourcesJsDir.mkdirs()

        copy {
            from(webJsDevBuildDir)
            into(resourcesJsDir)
            include("*.js")
            include("*.js.map")
        }

        println("Copied JS files to: ${resourcesJsDir.absolutePath}")
    }
}

tasks.named("run").configure {
    dependsOn(copyJsToResources)
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.html.builder)
    implementation(libs.ktor.server.compression)
    implementation(libs.kotlinx.html)
    implementation(libs.kotlin.css)
    implementation(libs.ktor.server.cio)
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
}
