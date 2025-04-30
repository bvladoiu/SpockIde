// file :webserver:main:Application.kt
package spock.lair

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import spock.lair.db.configureDatabases
import java.io.File

fun main() {
    println("Ktor server process working directory: ${File(".").absolutePath}")

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabases()
    configureSockets()
    configureTemplating()
    configureCssRoutes()
    configureRouting()
}
