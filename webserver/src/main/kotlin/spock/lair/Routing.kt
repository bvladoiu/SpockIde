// file :webserver:main:Routing.kt
package spock.lair

import io.ktor.server.application.*
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
import java.io.File

fun Application.configureRouting() {
    routing {

        staticFiles("/static", File("static")) {}

        get("/") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
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

        get("/home") {
            call.respondHtml {
                head {
                    title { +"Page Editor" }
                    link(rel = "stylesheet", href = "/static/styles.css", type = "text/css")
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

    }
}
