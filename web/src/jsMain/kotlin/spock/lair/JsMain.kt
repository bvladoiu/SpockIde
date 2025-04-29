// file :web:jsMain:JsMain.kt
package spock.lair

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.KeyboardEvent
import spock.lair.components.registerThemeSwitcher
import spock.lair.components.applySavedTheme

/**
 * Main entry point for the web module.
 * This function initializes the application when the page loads.
 */
fun main() {
    window.onload = {
        // Register component initializers
        registerThemeSwitcher()

        // Apply saved theme before initializing components
        applySavedTheme()

        // Initialize all components based on data-component attributes
        initComponents()

        // Set up editor functionality
        setupEditor()

        console.log("Web module initialized")
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
