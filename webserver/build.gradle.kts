plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinxSerialization)
}

group = "spock.lair"
version = "1.0.0"

application {
    mainClass = "spock.lair.ApplicationKt"
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sqldelight {
    databases {
        create("CommonDatabase") {
            packageName = "spock.lair.db.common"
        }
        create("ContadealDatabase") {
            packageName = "spock.lair.db.contadeal"
        }
        create("PrismaDatabase") {
            packageName = "spock.lair.db.prisma"
        }
        create("AcmeDatabase") {
            packageName = "spock.lair.db.acme"
        }
    }
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

    // SQLite and JDBC drivers
    implementation(libs.sqlite.jdbc)
    implementation(libs.sqldelight.sqlite.driver)
    implementation(libs.sqldelight.jdbc.driver)
}
