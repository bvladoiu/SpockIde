package spock.lair.tools.server

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*


fun Application.start(host: String, port: Int) {
    embeddedServer(CIO, port = port, host = host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureMonitoring()
    configureRouting()
}


