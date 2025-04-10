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
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("DevServerManagerScope"))
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
    println("Server::configureRouting")
    routing {
        get("/") {
            call.respondText("Hello!")
            println("Server::get/")
        }
    }
}


fun Application.configureSockets() {
    println("Server::configureSockets")
    install(WebSockets) {
        println("Server::install(WebSockets)")
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
    }
    routing {
        println("Server::routing")
        webSocket("/ws") {
            launch {
                println("Server::launch::handleOutgoing")
                handleOutgoing()
            }
            try {
                println("Server::handleIncoming")
                handleIncoming()
            } catch (e: CancellationException) {
                println("WebSocket session scope cancelled.")
            } catch (e: Exception) {
                println("Error in WebSocket session scope: ${e.message}")
            } finally {
                println("WebSocket session finished for client.")
            }
        }
    }
}

private suspend fun DefaultWebSocketServerSession.handleIncoming() {
    println("Server::handleIncoming")
    try {
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val text = frame.readText()
                println("Server Js->Jvm: $text")
                bridge.emit(text)
            }
        }
    } catch (e: ClosedReceiveChannelException) {
        println("Server WS Incoming: Closed.")
    } catch (e: CancellationException) {
        println("Server WS Incoming: Cancelled.")
    } catch (e: Exception) {
        println("Server WS Incoming Error: ${e::class.simpleName} - ${e.message}")
    }
}

private suspend fun DefaultWebSocketServerSession.handleOutgoing() {
    println("Server::handleOutgoing")
    try {
        println("Server::collect")
        bridge.collect { text ->
            try {
                println("Server Jvm->Js: $text")
                outgoing.send(Frame.Text(text))
            } catch (e: CancellationException) {
                println("Server::throw cancelation error $e")
                throw e
            } catch (e: Exception) {
                println("Server WS Send Error: ${e::class.simpleName} - ${e.message}")
            }
        }
    } catch (e: CancellationException) {
        println("Server WS Outgoing: Cancelled.")
    } catch (e: Exception) {
        println("Server WS Outgoing Error: ${e::class.simpleName} - ${e.message}")
    }
}