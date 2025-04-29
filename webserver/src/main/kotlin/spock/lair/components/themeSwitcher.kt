// file :webserver:main:components:themeSwitcher.kt
package spock.lair.components

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*

/**
 * Creates a theme switcher component.
 * This component allows users to toggle between light and dark themes.
 */
fun FlowContent.themeSwitch() {
    div {
        classes = setOf("theme-switcher")
        attributes["data-component"] = "theme-switcher"

        button {
            classes = setOf("theme-toggle")
            attributes["aria-label"] = "Toggle dark mode"
            attributes["title"] = "Toggle dark mode"

            span {
                classes = setOf("theme-toggle-icon", "light-icon")
                +"☀️"
            }
            span {
                classes = setOf("theme-toggle-icon", "dark-icon")
                +"🌙"
            }
        }
    }
}

/**
 * CSS DSL for theme switcher styles.
 * This function returns a CSSBuilder with all the styles for the theme switcher.
 */
fun themeSwitcherStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Theme switcher styles
        rule(".theme-toggle") {
            cursor = Cursor.pointer
            display = Display.inlineBlock
            padding(8.px)
            borderRadius = LinearDimension("var(--border-radius-circle)")
            transition("background-color var(--transition)")

            hover {
                backgroundColor = Color("var(--color-hover)")
            }
        }
        
        // Additional theme switcher styles can be added here
        rule(".theme-toggle-icon") {
            fontSize = LinearDimension("var(--font-size-base)")
            transition("opacity var(--transition)")
        }
        
        rule(".light-icon") {
            display = Display.inline
            
            rule("body.dark &") {
                display = Display.none
            }
        }
        
        rule(".dark-icon") {
            display = Display.none
            
            rule("body.dark &") {
                display = Display.inline
            }
        }
    }
}