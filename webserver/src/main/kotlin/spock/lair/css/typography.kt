package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for typography variables.
 * This function returns a CSSBuilder with all the typography variables.
 * This is meant to be inlined in the head of the HTML document.
 */
fun typographyVariables(): CSSBuilder {
    return CSSBuilder().apply {
        root {
            // Typography
            put("--font-family", "Arial, sans-serif")
            put("--font-size-base", "16px")
            put("--font-size-small", "14px")
            put("--font-size-large", "18px")
            put("--font-size-h1", "48px")
            put("--font-size-h2", "32px")
            put("--font-size-h3", "24px")
            put("--font-size-h4", "20px")
        }
    }
}

/**
 * CSS DSL for typography styles.
 * This function returns a CSSBuilder with all the typography styles.
 */
fun typographyStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Global typography styles
        body {
            fontFamily = "var(--font-family)"
            fontSize = LinearDimension("var(--font-size-base)")
            lineHeight = LineHeight("1.6")
            color = Color("var(--color-text)")
        }

        rule("h1, h2, h3, h4, h5, h6") {
            margin(top = 0.px, bottom = 20.px)
            fontWeight = FontWeight.bold
            lineHeight = LineHeight("1.2")
        }

        rule("h1") {
            fontSize = LinearDimension("var(--font-size-h1)")
        }

        rule("h2") {
            fontSize = LinearDimension("var(--font-size-h2)")
        }

        rule("h3") {
            fontSize = LinearDimension("var(--font-size-h3)")
        }

        rule("h4") {
            fontSize = LinearDimension("var(--font-size-h4)")
        }

        rule("p") {
            margin(top = 0.px, bottom = 20.px)
        }

        rule("a") {
            color = Color("var(--color-primary)")
            textDecoration = TextDecoration.none

            hover {
                color = Color("var(--color-primary-dark)")
                textDecoration = TextDecoration.none
                put("text-decoration", "underline")
            }
        }

        // Dark mode typography
        rule("[data-theme=\"dark\"] body") {
            color = Color("var(--color-text)")
        }

        rule("[data-theme=\"dark\"] a") {
            color = Color("var(--color-primary-light)")

            hover {
                color = Color.white
            }
        }
    }
}
