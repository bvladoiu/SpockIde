package spock.lair

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlin.time.Duration.Companion.seconds

object Server {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val engine = embeddedServer(CIO, port = EDITOR_PORT, host = HOST, module = Application::module)

    init {
        start()
        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
        })
    }
}

fun Server.start() {
    scope.launch {
        while (isActive) {
            try {
                engine.start(wait = false)
                break
            } catch (e: Exception) {
                delay(2500)
            }
        }
    }
}

fun Server.stop() {
    engine.stop(gracePeriodMillis = 1000, timeoutMillis = 5000)
    scope.cancel()
}



fun Application.module() {
    configureSockets()
    launch {
        configureRouting()
    }
}

fun Application.configureRouting() {
    log("Server::configureRouting")
    routing {
        get("/") {
            call.respondText("Hello!")
            log("Server::get/")
        }
    }
}


fun Application.configureSockets() {
    log("Server::configureSockets")
    install(WebSockets) {
        log("Server::install(WebSockets)")
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
    }
    routing {
        log("Server::routing")
        webSocket("/ws") {
            launch {
                log("Server::launch::handleOutgoing")
                handleOutgoing()
            }
            try {
                log("Server::handleIncoming")
                handleIncoming()
            } catch (e: CancellationException) {
                log("WebSocket session scope cancelled.")
            } catch (e: Exception) {
                log("Error in WebSocket session scope: ${e.message}")
            } finally {
                log("WebSocket session finished for client.")
            }
        }
    }
}

private suspend fun DefaultWebSocketServerSession.handleIncoming() {
    log("Server::handleIncoming")
    try {
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val text = frame.readText()
                log("Server Js->Jvm: $text")
                bridge.emit(text)
            }
        }
    } catch (e: ClosedReceiveChannelException) {
        log("Server WS Incoming: Closed.")
    } catch (e: CancellationException) {
        log("Server WS Incoming: Cancelled.")
    } catch (e: Exception) {
        log("Server WS Incoming Error: ${e::class.simpleName} - ${e.message}")
    }
}

private suspend fun DefaultWebSocketServerSession.handleOutgoing() {
    log("Server::handleOutgoing")
    try {
        log("Server::collect")
        bridge.collect { text ->
            try {
                log("Server Jvm->Js: $text")
                outgoing.send(Frame.Text(text))
            } catch (e: CancellationException) {
                log("Server::throw cancelation error $e")
                throw e
            } catch (e: Exception) {
                log("Server WS Send Error: ${e::class.simpleName} - ${e.message}")
            }
        }
    } catch (e: CancellationException) {
        log("Server WS Outgoing: Cancelled.")
    } catch (e: Exception) {
        log("Server WS Outgoing Error: ${e::class.simpleName} - ${e.message}")
    }
}