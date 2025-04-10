// In :klient-js:jsMain:main.js.kt
package spock.lair.ktor

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

val HOST = "localhost"
val PORT = 7989

object Klient {

    fun start(scope: CoroutineScope) {
        scope.launch {
            while (isActive) {
                try {

                    HttpClient(Js) { install(WebSockets) }
                        .webSocket(method = HttpMethod.Get, host = HOST, port = PORT, path = "/ws") {
                            handleReceived()
                        }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                } finally {
                    delay(3500)
                }
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.handleReceived() {
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = (frame.readText())
              //      bridge.emit(message)//should i just take a lambda parameter on start with the scope?
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            println("Klient incoming channel closed.")
        } catch (e: CancellationException) {
            println("Klient receiver cancelled.")
        } catch (e: Exception) {
            println("Klient receive error: ${e.message}")
        }
    }
}