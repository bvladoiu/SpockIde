// file :webserver:main:Routing.kt
package spock.lair

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        // Serve static JS files
        staticFiles("/static", File("static")) {}

        // Add caching headers for JS files
        get("/static/{filename}.js") {
            val filename = call.parameters["filename"] ?: return@get
            val file = File("static/$filename.js")
            if (file.exists()) {
                call.response.header("Cache-Control", "max-age=3600") // 1 hour cache
                call.respondFile(file)
            }
        }
    }
}
