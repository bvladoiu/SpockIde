package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for section styles.
 * This function returns a CSSBuilder with all the section styles.
 */
fun sectionStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Section description
        rule(".section-description") {
            fontSize = LinearDimension("var(--font-size-large)")
            marginBottom = 30.px
            textAlign = TextAlign.center
            color = Color("var(--color-text-light)")
            maxWidth = 800.px
            margin(LinearDimension.auto)
        }
    }
}
