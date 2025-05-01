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
    implementation(libs.graalvm.sdk)
    implementation(libs.graalvm.nativeimage)
}

// Custom task to build native image
tasks.register("buildNativeImage", Exec::class) {
    group = "build"
    description = "Builds a native image using GraalVM"

    dependsOn("assemble")

    doFirst {
        // Create directories for GraalVM native-image configuration if they don't exist
        val metaInfDir = project.projectDir.resolve("src/main/resources/META-INF/native-image")
        metaInfDir.mkdirs()

        // Create reflect-config.json if it doesn't exist
        val reflectConfigFile = metaInfDir.resolve("reflect-config.json")
        if (!reflectConfigFile.exists()) {
            reflectConfigFile.writeText("""
                [
                  {
                    "name": "spock.lair.ApplicationKt",
                    "allDeclaredConstructors": true,
                    "allPublicConstructors": true,
                    "allDeclaredMethods": true,
                    "allPublicMethods": true
                  },
                  {
                    "name": "kotlin.reflect.jvm.internal.ReflectionFactoryImpl",
                    "allDeclaredConstructors": true
                  },
                  {
                    "name": "io.ktor.server.cio.CIO",
                    "allDeclaredConstructors": true,
                    "allPublicConstructors": true,
                    "allDeclaredMethods": true,
                    "allPublicMethods": true
                  }
                ]
            """.trimIndent())
        }

        // Create resource-config.json if it doesn't exist
        val resourceConfigFile = metaInfDir.resolve("resource-config.json")
        if (!resourceConfigFile.exists()) {
            resourceConfigFile.writeText("""
                {
                  "resources": {
                    "includes": [
                      {"pattern": ".*\\.properties"},
                      {"pattern": "META-INF/services/.*"},
                      {"pattern": "META-INF/native-image/.*"},
                      {"pattern": "static/.*"},
                      {"pattern": "js/.*"},
                      {"pattern": "logback.xml"}
                    ]
                  }
                }
            """.trimIndent())
        }
    }

    // Set the command to run the GraalVM native-image tool
    // Note: This assumes GraalVM is installed and native-image is available in the PATH
    val jarFile = "${project.buildDir}/libs/${project.name}-${project.version}.jar"
    val outputDir = "${project.buildDir}/native"
    val outputName = "lair-webserver"

    commandLine(
        "native-image",
        "--no-fallback",
        "-H:+ReportExceptionStackTraces",
        "--initialize-at-build-time=ch.qos.logback,org.slf4j",
        "--initialize-at-run-time=io.netty,io.ktor",
        "-H:ReflectionConfigurationFiles=${project.projectDir}/src/main/resources/META-INF/native-image/reflect-config.json",
        "-H:ResourceConfigurationFiles=${project.projectDir}/src/main/resources/META-INF/native-image/resource-config.json",
        "-cp", jarFile,
        "-H:Name=$outputName",
        "-H:Path=$outputDir",
        "spock.lair.ApplicationKt"
    )

    doLast {
        println("Native image built successfully!")
        println("You can find the executable at: $outputDir/$outputName.exe")
    }
}
