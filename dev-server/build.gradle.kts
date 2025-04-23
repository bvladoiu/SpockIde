
plugins {
    kotlin("jvm")
    alias(libs.plugins.ktor)
    application
}

group = "spock.lair"
version = "1.0.0"


application {
    mainClass.set("spock.lair.MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}


dependencies {
    implementation(project(":tools"))
}