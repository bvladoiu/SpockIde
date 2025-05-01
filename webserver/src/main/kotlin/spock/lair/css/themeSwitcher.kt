// file :webserver:main:components:themeSwitcher.kt
package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*


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