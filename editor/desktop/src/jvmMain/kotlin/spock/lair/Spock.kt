package spock.lair

import androidx.compose.runtime.*
import spock.lair.App.content
import spock.lair.fileio.PersistentMap

val root = "../../root"


fun err(message: String) = content.value.plus("\n$message")

object App {

    fun execute() {
        when (content.value.type()) {
            "proxy" -> err(content.value)
            "browser" -> err(content.value)
            "storage" -> err(content.value)
            else -> content.value = "Welcome! "
        }
    }

    fun save() = PersistentMap("$root/${(content.value).type()}").save((content.value).key(), (content.value))
    fun load() = PersistentMap("$root/${(content.value).type()}").get((content.value).key()) ?: "n/a"
    fun info() {
        var info = System.getProperty("user.dir")
        content.value = info
    }

    var content: MutableState<String> = mutableStateOf("")

    val logEntries = mutableStateListOf<String>()

    var darkTheme by mutableStateOf(false)
        private set

    fun switchTheme() {
        darkTheme = !darkTheme
    }

    fun addLogEntry(entry: String) {
        //postMessage(entry)
        // bgScope.launch {
        logEntries.add(0, entry)
    }
}


/*suspend fun loadSite(path: String): String = withContext(Dispatchers.IO) {

    val baseDir = File(path)
    val file = File(baseDir, path)
    if (file.exists()) {
        file.readText()
    } else {
        error("Resource not found: $path")
    }
}*/

