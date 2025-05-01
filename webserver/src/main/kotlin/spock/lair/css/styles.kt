package spock.lair.css

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.css.*

fun mainStyles(): CSSBuilder {
    return CSSBuilder().apply {
        addStyles(themeStyles())
        addStyles(typographyStyles())
        addStyles(layoutStyles())
        addStyles(sectionStyles())
        addStyles(buttonStyles())
        addStyles(articleStyles())
        addStyles(langSelectStyles())
        addStyles(editorStyles())
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
