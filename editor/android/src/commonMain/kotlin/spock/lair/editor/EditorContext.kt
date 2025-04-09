package spock.lair.editor

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import spock.lair.bridge


object EditorContext {

    fun close(){
        bgScope.cancel()
    }
    val bgScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var content: MutableState<String> = mutableStateOf("")
        private set

    val logEntries = mutableStateListOf<String>()

    var darkTheme by mutableStateOf(false)
        private set

    fun switchTheme() = bgScope.launch {
        darkTheme = !darkTheme
        bridge.emit("Switched Theme -  dark: $darkTheme")
    }

    fun updateEditorContent(newContent: String) {
        bgScope.launch {
            content.value = newContent
        }
    }

    fun addLogEntry(entry: String) {
        //postMessage(entry)
        bgScope.launch {
            logEntries.add(0, entry)
        }
    }

    fun save() = bgScope.launch {
        bridge.emit("Save")
    }

    fun emit(arg: String) = bgScope.launch {
        bridge.emit(arg)
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


}