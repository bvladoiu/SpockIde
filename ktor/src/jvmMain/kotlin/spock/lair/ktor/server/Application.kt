package spock.lair.ktor.server

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*


fun mainJvm(host: String, port: Int) {
    embeddedServer(CIO, port = port, host = host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureMonitoring()
    configureRouting()
}


