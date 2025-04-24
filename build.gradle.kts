//lair(root):built.gradle.kts

// Task name constants
val dependencyUpdatesTask = "dependencyUpdates"
val copyJsStaticTask = "copyJsStaticDev"
val runWebserverTask = "runWebserver"
val runBrowserTask = "runBrowser"
val webJsBrowserProductionWebpackTask = ":web:jsBrowserProductionWebpack"
val webserverRunTask = ":webserver:run"
val browserJvmRunTask = ":browser:jvmRun"

plugins {
    alias(libs.plugins.benmanes.versions)
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.nodeGradle) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
}


tasks.named(dependencyUpdatesTask).configure {

}

val jsStaticDevDir = layout.buildDirectory.dir("../static")

tasks.register(copyJsStaticTask, DefaultTask::class) {
    group = "build"
    description = "Builds the :web development JS bundle and copies output to Ktor's static serving directory."
    dependsOn(webJsBrowserProductionWebpackTask)

    doLast {
        val jsDevBuildDir = project(":web").buildDir.resolve("kotlin-webpack/js/productionExecutable")
        jsStaticDevDir.get().asFile.mkdirs()
        copy {
            from(jsDevBuildDir)
            into(jsStaticDevDir)
            include("*.js")
            include("*.js.map")
        }
        println("Copied JS dev files to: ${jsStaticDevDir.get().asFile.absolutePath}")
    }
}

tasks.register(runWebserverTask, DefaultTask::class) {
    group = "application"
    description = "Runs the :webserver in development mode, serving static JS from the dev directory."
    dependsOn(copyJsStaticTask)
    val ktorRunTask = tasks.getByPath(webserverRunTask)
    (ktorRunTask as JavaExec).apply{
        jvmArgs = listOf(
            "-Dio.ktor.development=${project.hasProperty("development") || project.gradle.startParameter.taskNames.any { it.contains(runWebserverTask) } }",
        )
        workingDir = rootDir
    }
    dependsOn(ktorRunTask)
}

tasks.register(runBrowserTask, DefaultTask::class) {
    group = "application"
    description = "Runs the browser in development mode, using the same JS bundle as the webserver."
    dependsOn(copyJsStaticTask)
    val browserRunTask = tasks.getByPath(browserJvmRunTask)
    (browserRunTask as JavaExec).apply {
        workingDir = rootDir
    }
    dependsOn(browserRunTask)
}
