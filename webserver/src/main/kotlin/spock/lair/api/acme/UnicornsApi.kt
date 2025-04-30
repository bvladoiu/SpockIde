// file :webserver:main:api:acme:UnicornsApi.kt
package spock.lair.api.acme

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import spock.lair.db.TenantDatabaseManager
import spock.lair.db.acme.Unicorn
import spock.lair.db.acme.Unicorns
import java.io.File

/**
 * Data class for unicorn content from JSON files
 */
@Serializable
private data class UnicornContent(
    val title: String,
    val icon: String,
    val description: String
)

/**
 * Configure unicorns REST API routes.
 * This API provides CRUD operations for unicorns under the /acme/{locale}/unicorns path.
 */
fun Application.configureUnicornsApi() {
    routing {
        // Create a new unicorn
        post("/acme/{locale}/unicorns") {
            val unicorn = call.receive<Unicorn>()
            val locale = call.parameters["locale"] ?: "en"

            // Create a new unicorn in the database
            val id = TenantDatabaseManager.withTenant(call) { driver ->
                Unicorns.create(driver, unicorn)
            }

            // Create a new unicorn with the generated ID
            val createdUnicorn = Unicorn(
                id = id.toInt(),
                title = unicorn.title,
                description = unicorn.description,
                icon = unicorn.icon,
                index_order = unicorn.index_order
            )

            call.respond(HttpStatusCode.Created, createdUnicorn)
        }

        // Get a specific unicorn by ID
        get("/acme/{locale}/unicorns/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val locale = call.parameters["locale"] ?: "en"

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            // Get unicorn from database
            val unicorn = TenantDatabaseManager.withTenant(call) { driver ->
                Unicorns.read(driver, id)
            }

            if (unicorn != null) {
                // Try to load additional data from JSON file
                val jsonFile = File("static/acme/content/unicorns/$locale/${unicorn.title.lowercase().replace(" ", "_")}_unicorn.json")
                if (jsonFile.exists()) {
                    try {
                        val content = Json.decodeFromString<UnicornContent>(jsonFile.readText())
                        val enrichedUnicorn = unicorn.copy(
                            icon = content.icon,
                            description = content.description
                        )
                        call.respond(HttpStatusCode.OK, enrichedUnicorn)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.OK, unicorn)
                    }
                } else {
                    call.respond(HttpStatusCode.OK, unicorn)
                }
            } else {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
            }
        }

        // Get a list of unicorns with pagination
        get("/acme/{locale}/unicorns") {
            val locale = call.parameters["locale"] ?: "en"
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 5
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0

            // Validate limit
            if (limit < 0 || limit > 25) {
                call.respond(HttpStatusCode.BadRequest, "Limit must be between 0 and 25")
                return@get
            }

            // Get unicorns from database
            val unicorns = TenantDatabaseManager.withTenant(call) { driver ->
                Unicorns.read(driver)
            }

            // Get total count
            val totalCount = unicorns.size

            // Check if offset is out of bounds
            if (offset >= totalCount && totalCount > 0) {
                call.respond(HttpStatusCode.NotFound, "Offset out of bounds")
                return@get
            }

            // Apply pagination
            val paginatedUnicorns = unicorns
                .sortedBy { it.index_order }
                .drop(offset)
                .take(limit)
                .map { unicorn ->
                    // Try to load additional data from JSON file
                    val jsonFile = File("static/acme/content/unicorns/$locale/${unicorn.title.lowercase().replace(" ", "_")}_unicorn.json")
                    if (jsonFile.exists()) {
                        try {
                            val content = Json.decodeFromString<UnicornContent>(jsonFile.readText())
                            unicorn.copy(
                                icon = content.icon,
                                description = content.description
                            )
                        } catch (e: Exception) {
                            unicorn
                        }
                    } else {
                        unicorn
                    }
                }

            call.respond(HttpStatusCode.OK, paginatedUnicorns)
        }

        // Update a unicorn
        put("/acme/{locale}/unicorns/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val locale = call.parameters["locale"] ?: "en"
            val unicorn = call.receive<Unicorn>()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            // Update unicorn in database
            val updatedUnicorn = unicorn.copy(id = id)
            val success = TenantDatabaseManager.withTenant(call) { driver ->
                Unicorns.update(driver, updatedUnicorn)
            }
            
            if (!success) {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
                return@put
            }

            call.respond(HttpStatusCode.OK, updatedUnicorn)
        }

        // Delete a unicorn
        delete("/acme/{locale}/unicorns/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val locale = call.parameters["locale"] ?: "en"

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            // Delete unicorn from database
            val success = TenantDatabaseManager.withTenant(call) { driver ->
                Unicorns.delete(driver, id)
            }

            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
            }
        }
    }
}