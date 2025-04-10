package spock.lair.klient.jvm

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlin.jvm.Volatile

const val HOST = "localhost"
const val PORT = 7989
const val RECONNECT_DELAY_MS = 3500L

object Klient {
    @Volatile private var activeSession: DefaultClientWebSocketSession? = null
    private var client: HttpClient? = null

    fun start(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            client = client ?: HttpClient(CIO) { install(WebSockets) }
            try {
                while (isActive) {
                    var session: DefaultClientWebSocketSession? = null
                    try {
                        client?.webSocket(method = HttpMethod.Get, host = HOST, port = PORT, path = "/ws") {
                            session = this
                            activeSession = this
                            try {
                                for (frame in incoming) {
                                    // Removed call to onMessageReceived for Frame.Text
                                    if (frame is Frame.Close) {
                                        break
                                    }
                                    // Note: Received Frame.Text messages are now ignored
                                }
                            } catch (e: ClosedReceiveChannelException) {
                            } catch (e: CancellationException) { throw e
                            } catch (e: Exception) {
                            } finally {
                                if (activeSession == session) activeSession = null
                            }
                        }
                    } catch (e: Exception) {
                        if (activeSession == session) activeSession = null
                        if (e is CancellationException) throw e
                    }
                    if (isActive) { delay(RECONNECT_DELAY_MS) }
                }
            } finally {
                client?.close()
                activeSession = null
            }
        }
    }

    suspend fun sendMessage(message: String): Boolean {
        val session = activeSession
        if (session != null && session.isActive) {
            return try {
                session.send(Frame.Text(message))
                true
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                false
            }
        } else {
            return false
        }
    }
}