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

        staticFiles("/static", File("static")) {}

        get("/") {
            call.respondHtml {
                head {
                    // CSS is now served dynamically from the CSS DSL
                }

                body {
                    h1(classes = "page-title") { +"Ktor + Kotlin/JS Static Serving Demo" }
                    p { +"See developer console for JS output."}
                    script(src = "/static/web.js") {
                        attributes["defer"] = "true"
                    }
                }
            }
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
            call.respondHtml {
                head {
                    title { +"Page Editor" }
                    // CSS is now served dynamically from the CSS DSL
                    style { unsafe { +mainStyles().toString() } }
                }
                body {
                    div {
                        id = "page-content"
                        heroSection("hero-1", "Welcome to the Page Editor")
                        contentSection("section-1", "First Section", "This is the content of the first section.")
                        contentSection("section-2", "Second Section", "This is the content of the second section.")
                    }
                    script(src = "/static/web.js") {
                        attributes["defer"] = "true"
                    }
                }
            }
        }

        // API endpoints for page editor
        post("/api/save/{pageName}") {
            val pageName = call.parameters["pageName"] ?: return@post call.respondText("Missing page name", status = HttpStatusCode.BadRequest)
            val content = call.receiveText()

            val file = File("static/$pageName.html")
            file.writeText(content)

            call.respondText("Page saved successfully", status = HttpStatusCode.OK)
        }

        get("/api/load/{pageName}") {
            val pageName = call.parameters["pageName"] ?: return@get call.respondText("Missing page name", status = HttpStatusCode.BadRequest)

            val file = File("static/$pageName.html")
            if (!file.exists()) {
                call.respondText("Page not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val content = file.readText()
            call.respondText(content, ContentType.Text.Html)
        }

        post("/api/deploy/{pageName}") {
            val pageName = call.parameters["pageName"] ?: return@post call.respondText("Missing page name", status = HttpStatusCode.BadRequest)

            // For now, just log the deployment trigger
            application.log.info("Deployment triggered for $pageName")

            // Read the content from the saved file
            val file = File("static/$pageName.html")
            if (!file.exists()) {
                call.respondText("Page not found", status = HttpStatusCode.NotFound)
                return@post
            }

            // In a real implementation, you would process the HTML and deploy it
            // For now, we'll just mark it as deployable
            val deployableFile = File("static/$pageName.deployable.html")
            deployableFile.writeText(file.readText())

            call.respondText("Deployment triggered successfully", status = HttpStatusCode.OK)
        }

        get("/edit") {
            call.respondHtml {
                head {
                    title { +"Page Editor" }
                    // CSS is now served dynamically from the CSS DSL
                    style { unsafe { +mainStyles().toString() } }
                }
                body {
                    div {
                        id = "page-content"
                        heroSection("hero-1", "Welcome to the Page Editor")
                        contentSection("section-1", "First Section", "This is the content of the first section.")
                        contentSection("section-2", "Second Section", "This is the content of the second section.")
                    }
                    script(src = "/static/web.js") {
                        attributes["defer"] = "true"
                    }
                }
            }
        }

        get("/view/{pageName}") {
            val pageName = call.parameters["pageName"] ?: return@get call.respondText("Missing page name", status = HttpStatusCode.BadRequest)

            val file = File("static/$pageName.deployable.html")
            if (!file.exists()) {
                call.respondText("Deployed page not found", status = HttpStatusCode.NotFound)
                return@get
            }

            val content = file.readText()
            call.respondText(content, ContentType.Text.Html)
        }
    }
}
