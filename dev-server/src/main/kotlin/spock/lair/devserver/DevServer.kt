// File: src/main/kotlin/spock/lair/main.kt (or where your Ktor setup is)
package spock.lair // Or your main package

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import spock.lair.devserver.DevServerManager
import spock.lair.devserver.PlaywrightManager
import kotlin.time.Duration.Companion.seconds // Correct Duration import

object ServerConfig {
    val host = "0.0.0.0"
    val port = 8081
}

fun main() {
    println("Starting server on ${ServerConfig.host}:${ServerConfig.port}...")
    embeddedServer(CIO, port = ServerConfig.port, host = ServerConfig.host, module = Application::module)
        .start(wait = true)

    // Add shutdown hook to gracefully stop managers
    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown hook triggered.")
        DevServerManager.shutdown()
        PlaywrightManager.shutdown()
        println("Server shutdown complete.")
    })
}

fun Application.module() {
    configureSockets()
}

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = 15.seconds // Correct duration syntax
        timeout = 30.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") { // this: DefaultWebSocketSession
            println("WS Client connected: ${call.request.origin.remoteHost}")
            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val message = frame.readText().trim()
                        println("WS Received: $message")

                        // --- Routing Logic ---
                        val parts = message.split(" ", limit = 2)
                        val targetManagerName = parts.getOrNull(0)?.lowercase()
                        val commandForManager = parts.getOrNull(1) ?: "" // The rest is the command

                        when (targetManagerName) {
                            "dev", "proxy" -> { // Route 'dev' or 'proxy' to DevServerManager
                                // Pass the *full original message* or just the command part?
                                // Let's pass just the command part for simplicity within the manager handle
                                val command = if (targetManagerName == "proxy") {
                                    "proxy_" + commandForManager // Prepend 'proxy_' for clarity inside handle
                                } else {
                                    commandForManager // Or prefix with 'dev_' if needed
                                }
                                DevServerManager.handle(command, this)
                            }

                            "playwright", "pw" -> { // Route 'playwright' or 'pw' to PlaywrightManager
                                PlaywrightManager.handle(commandForManager, this)
                            }

                            null, "" -> { // Ignore empty messages
                                println("WS Received empty or blank message.")
                            }

                            else -> {
                                println("WS Unknown target manager: '$targetManagerName'")
                                send(Frame.Text("Error: Unknown command target '$targetManagerName'. Try 'dev', 'proxy', or 'playwright'."))
                            }
                        }
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                println("WS Client disconnected: ${call.request.origin.remoteHost}")
            } catch (e: Throwable) {
                println("WS Error for ${call.request.origin.remoteHost}: ${e::class.simpleName} - ${e.message}")
                kotlin.runCatching { close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Server processing error")) }
            } finally {
                println("WS Session ended for: ${call.request.origin.remoteHost}")
            }
        }
    }
}