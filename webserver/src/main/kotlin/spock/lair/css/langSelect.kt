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
            borderRadius = 4.px
            transition("background-color", 0.2.s)

            hover {
                put("background-color", "#f0f0f0")
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
            put("background-color", "#ffffff")
            borderRadius = 4.px
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
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
                put("background-color", "#f0f0f0")
            }
        }

        rule(".lang-select-option.active") {
            put("background-color", "#e6f7ff")
            fontWeight = FontWeight.bold
        }
    }
}
