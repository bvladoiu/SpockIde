// file :webserver:main:CssRoutes.kt
package spock.lair

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import spock.lair.css.*

/**
 * Configure CSS routes for the application.
 * In development mode, individual CSS files are served separately for easier debugging.
 * In production mode, a single aggregated CSS file is served.
 */
fun Application.configureCssRoutes() {
    val isDevelopment = environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false

    routing {
        // Production mode: serve aggregated CSS
        get("/static/css/app.css") {
            call.respondMainCss()
        }

        if (isDevelopment) {
            // Development mode: serve individual CSS files
            get("/static/css/dev/theme.css") {
                call.respondText(themeStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/typography.css") {
                call.respondText(typographyStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/layout.css") {
                call.respondText(layoutStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/section.css") {
                call.respondText(sectionStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/button.css") {
                call.respondText(buttonStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/article.css") {
                call.respondText(articleStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/lang-select.css") {
                call.respondText(langSelectStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/editor.css") {
                call.respondText(editorStyles().toString(), ContentType.Text.CSS)
            }

            get("/static/css/dev/dark-mode.css") {
                call.respondText(darkModeStyles().toString(), ContentType.Text.CSS)
            }
        }
    }
}
