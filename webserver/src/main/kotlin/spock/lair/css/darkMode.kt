package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for dark mode styles.
 * This function returns a CSSBuilder with all the dark mode styles.
 */
fun darkModeStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Dark mode variables
        rule("[data-theme=\"dark\"]") {
            put("--theme-background", "#333333")
            put("--theme-text", "#ffffff")
            put("--color-background", "#222222")
            put("--color-background-light", "#333333")
            put("--color-text", "#ffffff")
            put("--color-text-light", "#cccccc")
            put("--color-border", "#444444")
        }

        // Dark mode body
        rule("[data-theme=\"dark\"] body") {
            backgroundColor = Color("var(--color-background)")
            color = Color("var(--color-text)")
        }

        // Dark mode links
        rule("[data-theme=\"dark\"] a") {
            color = Color("var(--color-primary-light)")

            hover {
                color = Color.white
            }
        }

        // Dark mode buttons
        rule("[data-theme=\"dark\"] .button") {
            backgroundColor = Color("var(--color-primary-dark)")
            color = Color.white

            hover {
                backgroundColor = Color("var(--color-primary)")
            }
        }

        // Dark mode sections
        rule("[data-theme=\"dark\"] .section") {
            backgroundColor = Color("var(--color-background-light)")
        }

        // Dark mode cards
        rule("[data-theme=\"dark\"] .post-card, [data-theme=\"dark\"] .person-card, [data-theme=\"dark\"] .competence-item") {
            backgroundColor = Color("var(--color-background-light)")
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.3)")

            hover {
                put("box-shadow", "0 10px 20px rgba(0, 0, 0, 0.5)")
            }
        }

        // Dark mode social links
        rule("[data-theme=\"dark\"] .social-link") {
            backgroundColor = Color("#444444")
            color = Color("#cccccc")

            hover {
                backgroundColor = Color("var(--color-primary)")
                color = Color.white
            }
        }
    }
}