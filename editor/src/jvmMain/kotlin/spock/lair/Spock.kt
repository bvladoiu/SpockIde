package spock.lair

import spock.lair.editor.Editor
import spock.lair.editor.Editor.item
import spock.lair.editor.Editor.log
import spock.lair.fileio.Storage
import spock.lair.strings.id
import spock.lair.strings.type
import spock.lair.tools.playwright.Browser


const val script = "script"

object Spock {

    fun cli(item: String) {
        if (item.id() == "script") {
            when (item().type()) {
                "editor" -> Editor.cli(item)
                "browser" -> Browser.cli(item())
                "proxy" -> log("Proxy : TODO")
                "storage" -> Storage.cli("Storage : TODO")
                else -> {
                    log("app: unknown:${item()}!")
                }
            }
        }
    }
}


