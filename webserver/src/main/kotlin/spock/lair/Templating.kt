// file :webserver:main:Templating.kt
package spock.lair

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
import spock.lair.css.*

fun FlowContent.heroSection(id: String, title: String) {
    div {
        attributes["data-component"] = "heroSection"
        attributes["data-id"] = id
        h1 {
            attributes["data-editable"] = "true"
            +title
        }
    }
}

fun FlowContent.contentSection(id: String, title: String, initialContent: String) {
    div {
        attributes["data-component"] = "contentSection"
        attributes["data-id"] = id
        h2 {
            attributes["data-editable"] = "true"
            +title
        }
        p {
            attributes["data-editable"] = "true"
            +initialContent
        }
    }
}

fun Application.configureTemplating() {
    routing {
        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }
        get("/styles.css") {
            call.respondMainCss()
        }

        get("/html-css-dsl") {
            call.respondHtml {
                head {
                    // CSS is now served dynamically from the CSS DSL
                    style { unsafe { +mainStyles().toString() } }
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor!"
                    }
                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
