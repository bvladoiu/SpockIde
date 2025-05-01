package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for critical inline styles.
 * This function returns a CSSBuilder with essential styles that should be inlined in every page.
 * These styles are critical for initial rendering and reference variables from theme.kt.
 */
fun inlineStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Import critical theme styles
        val themeStyles = themeStyles()

        // Critical Global Styles (from layout.kt)
        rule("*, *::before, *::after") {
            boxSizing = BoxSizing.borderBox
        }

        // Critical Typography Styles (from typography.kt)
        body {
            fontFamily = "var(--font-family)"
            fontSize = LinearDimension("var(--font-size-base)")
            lineHeight = LineHeight("1.6")
            color = Color("var(--color-text)")
            backgroundColor = Color("var(--color-background)")
            margin(0.px)
            padding(0.px)
            transition("color var(--transition), background-color var(--transition)")
        }

        // Critical Heading Styles (from typography.kt)
        rule("h1, h2, h3, h4, h5, h6") {
            margin(top = 0.px, bottom = LinearDimension("var(--spacing-md)"))
            fontWeight = FontWeight.bold
            lineHeight = LineHeight("1.2")
            color = Color("var(--color-text)")
        }

        // Critical Link Styles (from typography.kt)
        rule("a") {
            color = Color("var(--color-primary)")
            textDecoration = TextDecoration.none
            transition("color var(--transition)")

            hover {
                color = Color("var(--color-primary-dark)")
            }
        }

        // Critical Container Styles (from layout.kt)
        rule("#page-content") {
            maxWidth = LinearDimension("var(--container-width)")
            margin(LinearDimension.auto)
            padding(LinearDimension("var(--spacing-md)"))
        }

        // Import theme switcher styles
        val themeSwitcherStyles = themeSwitcherStyles()
    }
}
