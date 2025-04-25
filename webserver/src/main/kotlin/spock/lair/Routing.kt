// file :webserver:main:Routing.kt
package spock.lair

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
import spock.lair.css.*
import java.io.File

fun Application.configureRouting() {
    routing {

        // Serve static files from language-specific directories
        staticFiles("/static/en", File("static/en")) {}
        staticFiles("/static/de", File("static/de")) {}

        // Serve common static files
        staticFiles("/static/common", File("static/common")) {}

        get("/") {
            // Redirect to the default locale (English) home page
            call.respondRedirect("/en/home")
        }

        // Dynamic HTML endpoint with /locale/page_name pattern
        get("/{locale}/{pageName}") {
            val locale = call.parameters["locale"] ?: "en"
            val pageName = call.parameters["pageName"] ?: "home"

            val pageObject = PageObject(locale, pageName)

            call.respondHtml {
                scaffold(pageObject) {
                    when (pageName) {
                        "home" -> {
                            hero()
                            competences()
                            latest()
                        }
                        "press", "dev-blog" -> {
                            posts()
                        }
                        "company" -> {
                            hero()
                            team()
                        }
                        "article" -> {
                            // Article page will be implemented later
                            section("article") {
                                h1 { +"Article page coming soon" }
                            }
                        }
                        "contact" -> {
                            // Contact page will be implemented later
                            section("contact") {
                                h1 { +"Contact page coming soon" }
                            }
                        }
                        else -> {
                            section("not-found") {
                                h1 { +"Page not found" }
                                p { +"The requested page '$pageName' does not exist." }
                            }
                        }
                    }
                }
            }
        }

        get("/home") {
            // Redirect to the default locale (English) home page
            call.respondRedirect("/en/home")
        }

        // API endpoints for page editor
        post("/api/save/{locale}/{pageName}") {
            val locale = call.parameters["locale"] ?: "en"
            val pageName = call.parameters["pageName"] ?: return@post call.respondText("Missing page name", status = HttpStatusCode.BadRequest)
            val content = call.receiveText()

            val file = File("static/$locale/$pageName.html")
            file.writeText(content)

            call.respondText("Page saved successfully", status = HttpStatusCode.OK)
        }

        get("/api/load/{locale}/{pageName}") {
            val locale = call.parameters["locale"] ?: "en"
            val pageName = call.parameters["pageName"] ?: return@get call.respondText("Missing page name", status = HttpStatusCode.BadRequest)

            val file = File("static/$locale/$pageName.html")
            if (!file.exists()) {
                call.respondText("Page not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val content = file.readText()
            call.respondText(content, ContentType.Text.Html)
        }

        post("/api/deploy/{locale}/{pageName}") {
            val locale = call.parameters["locale"] ?: "en"
            val pageName = call.parameters["pageName"] ?: return@post call.respondText("Missing page name", status = HttpStatusCode.BadRequest)

            // For now, just log the deployment trigger
            application.log.info("Deployment triggered for $locale/$pageName")

            // Read the content from the saved file
            val file = File("static/$locale/$pageName.html")
            if (!file.exists()) {
                call.respondText("Page not found", status = HttpStatusCode.NotFound)
                return@post
            }

            // In a real implementation, you would process the HTML and deploy it
            // For now, we'll just mark it as deployable
            val deployableFile = File("static/$locale/$pageName.deployable.html")
            deployableFile.writeText(file.readText())

            call.respondText("Deployment triggered successfully", status = HttpStatusCode.OK)
        }

        get("/edit") {
            // Redirect to the default locale (English) edit page
            call.respondRedirect("/en/edit")
        }

        get("/{locale}/edit") {
            val locale = call.parameters["locale"] ?: "en"

            call.respondHtml {
                head {
                    title { +"Page Editor" }
                    // Critical inline styles loaded first for better performance
                    style { unsafe { +inlineStyles().toString() } }
                    // Main CSS styles loaded after inline styles
                    style { unsafe { +mainStyles().toString() } }
                }
                body {
                    div {
                        id = "page-content"
                        heroSection("hero-1", "Welcome to the Page Editor")
                        contentSection("section-1", "First Section", "This is the content of the first section.")
                        contentSection("section-2", "Second Section", "This is the content of the second section.")
                    }
                    script(src = "/static/common/web.js") {
                        attributes["defer"] = "true"
                    }
                }
            }
        }

        get("/view/{pageName}") {
            // Redirect to the default locale (English) view page
            val pageName = call.parameters["pageName"] ?: return@get call.respondText("Missing page name", status = HttpStatusCode.BadRequest)
            call.respondRedirect("/view/en/$pageName")
        }

        get("/view/{locale}/{pageName}") {
            val locale = call.parameters["locale"] ?: "en"
            val pageName = call.parameters["pageName"] ?: return@get call.respondText("Missing page name", status = HttpStatusCode.BadRequest)

            val file = File("static/$locale/$pageName.deployable.html")
            if (!file.exists()) {
                call.respondText("Deployed page not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val content = file.readText()
            call.respondText(content, ContentType.Text.Html)
        }
    }
}
