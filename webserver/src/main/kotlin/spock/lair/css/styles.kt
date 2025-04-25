package spock.lair.css

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for styling the website.
 * This function returns a CSSBuilder with all the styles for the website.
 * It combines all the other CSS DSL components.
 */
fun mainStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Add theme styles
        addStyles(themeStyles())

        // Add typography styles
        addStyles(typographyStyles())

        // Add layout styles
        addStyles(layoutStyles())

        // Add section styles
        addStyles(sectionStyles())

        // Add button styles
        addStyles(buttonStyles())

        // Add component styles
        addStyles(componentStyles())

        // Add article styles
        addStyles(articleStyles())

        // Add language selector styles
        addStyles(langSelectStyles())

        // Add editor styles
        addStyles(editorStyles())

        // Add dark mode styles
        addStyles(darkModeStyles())
    }
}

/**
 * Helper function to add styles from another CSSBuilder.
 */
private fun CSSBuilder.addStyles(other: CSSBuilder) {
    // Add all the CSS rules from the other builder
    rules.addAll(other.rules)
    // Add all the declarations from the other builder
    declarations.putAll(other.declarations)
}

/**
 * Extension function to respond with the main styles.
 */
suspend fun ApplicationCall.respondMainCss() {
    this.respondText(mainStyles().toString(), ContentType.Text.CSS)
}
