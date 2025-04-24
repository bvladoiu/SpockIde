package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for editor-specific styles.
 * This function returns a CSSBuilder with all the editor-specific styles.
 */
fun editorStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Editor mode
        rule(".editor-mode body") {
            backgroundColor = Color("var(--theme-background)")
            color = Color("var(--theme-text)")
            padding(20.px)
        }

        // Editable elements
        rule("[data-editable=\"true\"]:hover") {
            put("outline", "2px dashed #007bff")
            cursor = Cursor.pointer
        }

        rule(".is-editing [data-editable=\"true\"]") {
            put("outline", "2px solid #007bff")
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