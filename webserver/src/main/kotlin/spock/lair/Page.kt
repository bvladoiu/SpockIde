package spock.lair

import kotlinx.html.*
import spock.lair.css.inlineStyles
import spock.lair.html.scaffold


fun HTML.page(pageObject: PageObject, isDevelopment: Boolean = false) {
    head {
        title { +"Prisma Softwaer Home" }
        meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
        meta(charset = "UTF-8")

        if (isDevelopment) {
            // In development mode, load individual CSS files for easier debugging
            link(rel = "stylesheet", href = "/static/css/dev/theme.css")
            link(rel = "stylesheet", href = "/static/css/dev/typography.css")
            link(rel = "stylesheet", href = "/static/css/dev/layout.css")
            link(rel = "stylesheet", href = "/static/css/dev/section.css")
            link(rel = "stylesheet", href = "/static/css/dev/button.css")
            link(rel = "stylesheet", href = "/static/css/dev/component.css")
            link(rel = "stylesheet", href = "/static/css/dev/article.css")
            link(rel = "stylesheet", href = "/static/css/dev/lang-select.css")
            link(rel = "stylesheet", href = "/static/css/dev/editor.css")
            link(rel = "stylesheet", href = "/static/css/dev/dark-mode.css")
        } else {
            // In production mode, include critical inline styles and load the aggregated CSS
            style { unsafe { +inlineStyles().toString() } }
            link(rel = "stylesheet", href = "/static/css/app.css")
        }
    }
    body {
        scaffold(pageObject) {
            section("hero") { }
            section("competences") { }
            section("latest") { }

        }
        script(src = "/static/common/web.js") {
            attributes["defer"] = "true"
        }
        script(src = "/static/main.js") {
            attributes["defer"] = "true"
        }
    }
}