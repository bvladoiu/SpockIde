//:common:commonMain:shared.kt
package spock.lair

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

private val bridgeScope = CoroutineScope(Dispatchers.Default)

/*@Serializable
data class Objekt(
    val type: String,
    val style: String = "default",
    val id: String? = null,
    val title: String? = null,
    val props: Map<String, String> = emptyMap(),
    val states: Map<String, Map<String, String>> = emptyMap()
)*/

const val PORT = 8021
const val EDITOR_PORT = 8020
const val HOST = "localhost"
const val LIVE = "/live"

const val NODE = "node"


val bridge = MutableSharedFlow<String>(extraBufferCapacity = 64)

fun emmit(message: String) = bridgeScope.launch{ bridge.emit(message) }

