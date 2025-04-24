package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for layout styles.
 * This function returns a CSSBuilder with all the layout styles.
 */
fun layoutStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Global layout styles
        rule("*, *::before, *::after") {
            boxSizing = BoxSizing.borderBox
        }

        rule("img") {
            maxWidth = 100.pct
            height = LinearDimension.auto
        }

        // Page content
        rule("#page-content") {
            maxWidth = LinearDimension("var(--container-width)")
            margin(LinearDimension.auto)
            padding(20.px)
        }

        // Sections
        rule(".section") {
            marginBottom = 60.px
        }

        rule(".section-title") {
            fontSize = LinearDimension("var(--font-size-h2)")
            marginBottom = 30.px
            textAlign = TextAlign.center
        }

        // Editor components
        rule("[data-component=\"heroSection\"]") {
            padding(vertical = 40.px, horizontal = 20.px)
            backgroundColor = Color("#f5f5f5")
            marginBottom = 20.px
            borderRadius = 8.px
        }

        rule("[data-component=\"contentSection\"]") {
            padding(20.px)
            backgroundColor = Color.white
            marginBottom = 20.px
            border = "1px solid #e0e0e0"
            borderRadius = 8.px
        }

        // Dark mode layout
        rule("[data-theme=\"dark\"] #page-content") {
            backgroundColor = Color("var(--color-background)")
        }

        rule("[data-theme=\"dark\"] .section") {
            backgroundColor = Color("var(--color-background-light)")
        }
    }
}