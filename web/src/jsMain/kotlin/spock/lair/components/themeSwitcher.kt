// file :web:jsMain:components:themeSwitcher.kt
package spock.lair.components

import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import spock.lair.registerComponent

/**
 * Registers the theme switcher component.
 * This is called during application initialization.
 */
fun registerThemeSwitcher() {
    registerComponent("theme-switcher") { element ->
        val toggleButton = element.querySelector(".theme-toggle") as? HTMLElement ?: return@registerComponent
        
        // Add click event listener to toggle button
        toggleButton.onclick = {
            toggleTheme()
            false
        }
    }
    
    // Apply saved theme on page load
    applySavedTheme()
}

/**
 * Toggles between light and dark themes.
 * This function adds or removes the "dark" class from the body element and saves the theme preference to localStorage.
 */
fun toggleTheme() {
    val body = document.body ?: return
    val isDarkMode = body.classList.contains("dark")
    
    if (isDarkMode) {
        // Switch to light mode
        body.classList.remove("dark")
        localStorage.setItem("theme", "light")
    } else {
        // Switch to dark mode
        body.classList.add("dark")
        localStorage.setItem("theme", "dark")
    }
}

/**
 * Applies the saved theme from localStorage.
 * If no theme is saved, it uses the system preference.
 */
fun applySavedTheme() {
    val body = document.body ?: return
    val savedTheme = localStorage.getItem("theme")
    
    when {
        savedTheme == "dark" -> {
            body.classList.add("dark")
        }
        savedTheme == "light" -> {
            body.classList.remove("dark")
        }
        else -> {
            // Use system preference if no saved theme
            val prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches
            if (prefersDark) {
                body.classList.add("dark")
            }
        }
    }
}