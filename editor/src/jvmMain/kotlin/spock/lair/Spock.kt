package spock.lair

import androidx.compose.runtime.*
import spock.lair.App.content
import spock.lair.App.int
import spock.lair.App.items
import spock.lair.App.log

import spock.lair.fileio.PersistentMap
import spock.lair.strings.*
import spock.lair.tools.playwright.BrowserCli
import java.io.BufferedReader
import java.io.StringReader

val storage = "localstorage"


object App {
    var int = 0;
    fun log(msg: String) = content.value.plus(msg)

    fun addEntries(text: String) {
        val reader = StringReader(text)
        val bufferedReader = BufferedReader(reader)
        var line: String?
        try {
            while (bufferedReader.readLine().also { line = it } != null) {
                line?.let { items.add(it) }
            }
        } catch (e: Exception) {
            println("Error reading lines: ${e.message}")
        } finally {
            try {
                bufferedReader.close()
            } catch (e: Exception) {
                println("Error closing reader: ${e.message}")
            }
        }
    }


    fun run() {
        mainMenu()
        when (content.value.type()) {
            "proxy" -> log("Proxy : TODO")
            "browser" -> BrowserCli.run(content.value)
            "storage" -> log("Storage : TODO")
            else -> {
                log("App: unknown: ${content.value.type()}")
            }
        }
    }

    fun save() {
        PersistentMap("$storage/${(content.value).type()}").save((content.value).key(), (content.value))
    }

    fun load() {
        val results = PersistentMap("$storage/${(content.value).type()}").get((content.value).key()) ?: "n/a"
        results.lines().forEach { items.add(it) }
    }

    fun info() {
        var info = System.getProperty("user.dir")
        content.value = info
    }

    var content: MutableState<String> = mutableStateOf("")
    var selected: MutableState<String> = mutableStateOf("")
    val items = mutableStateListOf<String>()

    var darkTheme by mutableStateOf(false)
        private set

    fun switchTheme() {
        darkTheme = !darkTheme
    }

}

fun mainMenu() {
    items.clear()
    items.add("Browser")
    items.add("Proxyr")
    items.add("Worker")
    items.add("Page")
    log("Loaded Menu")
}