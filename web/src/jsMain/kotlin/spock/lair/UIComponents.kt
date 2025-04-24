package spock.lair

import kotlinx.browser.document

fun showShortcutsModal() {
    val existingModal = document.getElementById("shortcuts-modal")
    if (existingModal != null) {
        document.body?.removeChild(existingModal)
        return
    }

    val overlay = element("div") {
        attribute("id", "shortcuts-overlay")
        style {
            overlay()
        }
        onClick { event ->
            event.stopPropagation()
            document.getElementById("shortcuts-modal")?.let {
                document.body?.removeChild(it)
            }
            document.body?.removeChild(document.getElementById("shortcuts-overlay")!!)
        }
    }
    val modal = element("div") {
        attribute("id", "shortcuts-modal")
        style {
            modal()
        }
        onClick { event ->
            event.stopPropagation()
        }
    }

    val header = element("div") {
        style {
            modalHeader()
        }
    }

    val title = element("h2") {
        text("Keyboard Shortcuts")
        style {
            modalTitle()
        }
    }

    val closeButton = element("button") {
        text("×")
        style {
            closeButton()
        }
        onClick { event ->
            event.stopPropagation()
            document.getElementById("shortcuts-modal")?.let {
                document.body?.removeChild(it)
            }
            document.body?.removeChild(document.getElementById("shortcuts-overlay")!!)
        }
    }

    header.appendChild(title)
    header.appendChild(closeButton)

    val content = element("div") {
        style {
            modalContent()
        }
    }

    val shortcuts = mapOf(
        "Ctrl+1" to "Show this shortcuts dialog",
        "Ctrl+3" to "Enable edit mode",
        "Ctrl+4" to "Disable edit mode",
        "Ctrl+5" to "Save page",
        "Ctrl+8" to "Load page",
        "Ctrl+9" to "Deploy page"
    )

    shortcuts.forEach { (key, description) ->
        val shortcutItem = element("div") {
            style {
                shortcutItem()
            }
        }

        val keyElement = element("span") {
            text(key)
            style {
                shortcutKey()
            }
        }

        val descriptionElement = element("span") {
            text(description)
        }

        shortcutItem.appendChild(keyElement)
        shortcutItem.appendChild(descriptionElement)
        content.appendChild(shortcutItem)
    }

    modal.appendChild(header)
    modal.appendChild(content)
    
    document.body?.appendChild(overlay)
    document.body?.appendChild(modal)
}