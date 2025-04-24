package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for button styles.
 * This function returns a CSSBuilder with all the button styles.
 */
fun buttonStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Button styles
        rule(".button") {
            display = Display.inlineBlock
            padding(vertical = 10.px, horizontal = 20.px)
            backgroundColor = Color("var(--color-primary)")
            color = Color.white
            borderRadius = LinearDimension("var(--border-radius-sm)")
            border = "none"
            cursor = Cursor.pointer
            fontWeight = FontWeight.bold
            transition("all", 0.3.s)

            hover {
                backgroundColor = Color("var(--color-primary-dark)")
                transform { scale(1.05) }
            }
        }

        // Hero CTA button
        rule(".hero-cta") {
            fontSize = LinearDimension("var(--font-size-large)")
            padding(vertical = 12.px, horizontal = 30.px)
        }

        // Dark mode button styles
        rule("[data-theme=\"dark\"] .button") {
            backgroundColor = Color("var(--color-primary-dark)")
            color = Color.white

            hover {
                backgroundColor = Color("var(--color-primary)")
            }
        }

        // Editor buttons
        rule(".add-button, .remove-button") {
            backgroundColor = Color("#007bff")
            color = Color.white
            border = "none"
            borderRadius = 50.pct
            width = 30.px
            height = 30.px
            fontSize = 18.px
            cursor = Cursor.pointer
            margin(5.px)
            display = Display.none
        }

        rule(".is-editing .add-button, .is-editing .remove-button") {
            display = Display.inlineBlock
        }
    }
}
