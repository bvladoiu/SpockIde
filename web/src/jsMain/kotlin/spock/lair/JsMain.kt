// file :web:jsMain:JsMain.kt
package spock.lair

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.KeyboardEvent

fun main() {
    window.onload = {
        setupEditor()
    }
}

fun setupEditor() {
    document.addEventListener("keydown", { event ->
        val e = event as KeyboardEvent
        if (e.ctrlKey) {
            when (e.key) {
                "1" -> {
                    e.preventDefault()
                    showShortcutsModal()
                }

                "3" -> {
                    e.preventDefault()
                    enableEditMode()
                }

                "4" -> {
                    e.preventDefault()
                    disableEditMode()
                }

                "5" -> {
                    e.preventDefault()
                    savePage()
                }

                "8" -> {
                    e.preventDefault()
                    loadPage()
                }

                "9" -> {
                    e.preventDefault()
                    deployPage()
                }
            }
        }
    })
    console.log("Editor initialized")
}
