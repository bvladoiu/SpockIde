package spock.lair.devserver

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import spock.lair.ktor.server.configureRouting
import spock.lair.ktor.server.configureSockets

object DevServer{
    val host = "localhost"
    val port = 8081
}

fun DevServer.start() {
    embeddedServer(CIO, port = port, host = host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureMonitoring()
    configureRouting()
}


/*
@Serializable
data class Objekt(
    val type: String,
    val style: String = "default",
    val id: String? = null,
    val title: String? = null,
    val props: Map<String, String> = emptyMap(),
    val states: Map<String, Map<String, String>> = emptyMap()
)


*/
