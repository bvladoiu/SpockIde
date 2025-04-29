package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for styling language selector.
 * This function returns a CSSBuilder with all the styles for language selector.
 */
fun langSelectStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Language selector container
        rule(".lang-select") {
            display = Display.flex
            alignItems = Align.center
            position = Position.relative
            marginLeft = 20.px
        }

        rule(".lang-select-current") {
            display = Display.flex
            alignItems = Align.center
            cursor = Cursor.pointer
            padding(vertical = 5.px, horizontal = 10.px)
            borderRadius = LinearDimension("var(--border-radius-sm)")
            transition("background-color", 0.2.s)

            hover {
                backgroundColor = Color("var(--color-hover)")
            }
        }

        rule(".lang-select-flag") {
            width = 20.px
            height = 15.px
            marginRight = 8.px
            objectFit = ObjectFit.cover
        }

        rule(".lang-select-code") {
            fontSize = 14.px
            fontWeight = FontWeight.bold
            textTransform = TextTransform.uppercase
        }

        rule(".lang-select-arrow") {
            marginLeft = 5.px
            fontSize = 12.px
            transition("transform", 0.2.s)
        }

        rule(".lang-select.open .lang-select-arrow") {
            transform {
                rotate(180.deg)
            }
        }

        rule(".lang-select-dropdown") {
            position = Position.absolute
            top = 100.pct
            right = 0.px
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-sm)")
            put("box-shadow", "var(--shadow-md)")
            minWidth = 120.px
            zIndex = 100
            overflow = Overflow.hidden
            maxHeight = 0.px
            opacity = 0
            transition("all", 0.3.s)
            pointerEvents = PointerEvents.none
        }

        rule(".lang-select.open .lang-select-dropdown") {
            maxHeight = 300.px
            opacity = 1
            pointerEvents = PointerEvents.auto
        }

        rule(".lang-select-option") {
            display = Display.flex
            alignItems = Align.center
            padding(vertical = 8.px, horizontal = 10.px)
            cursor = Cursor.pointer
            transition("background-color", 0.2.s)

            hover {
                backgroundColor = Color("var(--color-hover)")
            }
        }

        rule(".lang-select-option.active") {
            backgroundColor = Color("var(--color-active)")
            fontWeight = FontWeight.bold
        }
    }
}
