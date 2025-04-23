//lair(root):built.gradle.kts
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

tasks.named("dependencyUpdates").configure {

}

val jsStaticDevDir = layout.buildDirectory.dir("../static")

tasks.register("copyJsStaticForDev", DefaultTask::class) {
    group = "build"
    description = "Builds the :web development JS bundle and copies output to Ktor's static serving directory."
    dependsOn(":web:jsBrowserProductionWebpack")

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

tasks.register("runWebserverDev", DefaultTask::class) {
    group = "application"
    description = "Runs the :webserver in development mode, serving static JS from the dev directory."
    dependsOn("copyJsStaticForDev")
    val ktorRunTask = tasks.getByPath(":webserver:run")
    (ktorRunTask as JavaExec).apply{
        jvmArgs = listOf(
            "-Dio.ktor.development=${project.hasProperty("development") || project.gradle.startParameter.taskNames.any { it.contains("runWebserverDev") } }",
        )
        workingDir = rootDir
    }
    dependsOn(ktorRunTask)
}
