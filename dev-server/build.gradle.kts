
plugins {
    application
    kotlin("jvm")
    alias(libs.plugins.ktor)
}

group = "spock.lair"
version = "1.0"


application {
    mainClass.set("spock.lair.devserver.MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}


dependencies {
    implementation(project(":ktor"))
}