// file :webserver:main:api:UnicornsApi.kt
package spock.lair.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import spock.lair.db.TenantDatabaseManager
import spock.lair.db.app.Unicorn
import spock.lair.db.app.UnicornsDao
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
 * This API provides CRUD operations for unicorns under the /{locale}/unicorns path.
 */
fun Application.configureUnicornsApi() {
    routing {
        // Create a new unicorn
        post("/{locale}/unicorns") {
            val unicorn = call.receive<Unicorn>()
            val locale = call.parameters["locale"] ?: "en"

            // Generate JSON path
            val jsonPath = "static/content/unicorns/$locale/${unicorn.title.lowercase().replace(" ", "_")}_unicorn.json"
            val unicornWithJsonPath = unicorn.copy(jsonPath = jsonPath)

            // Create a new unicorn in the database
            val id = TenantDatabaseManager.withTenant(call) { driver ->
                UnicornsDao.create(driver, unicornWithJsonPath)
            }

            // Create a new unicorn with the generated ID
            val createdUnicorn = unicornWithJsonPath.copy(id = id.toInt())

            // Create JSON file
            try {
                val jsonDir = File("static/content/unicorns/$locale")
                jsonDir.mkdirs()

                val jsonContent = UnicornContent(
                    title = createdUnicorn.title,
                    icon = createdUnicorn.icon ?: "",
                    description = createdUnicorn.description
                )

                File(jsonPath).writeText(Json.encodeToString(jsonContent))
            } catch (e: Exception) {
                // Log error but continue
                println("Error creating JSON file: ${e.message}")
            }

            call.respond(HttpStatusCode.Created, createdUnicorn)
        }

        // Get a specific unicorn by ID
        get("/{locale}/unicorns/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val locale = call.parameters["locale"] ?: "en"

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            // Get unicorn from database
            val unicorn = TenantDatabaseManager.withTenant(call) { driver ->
                UnicornsDao.read(driver, id)
            }

            if (unicorn != null) {
                // Try to load additional data from JSON file
                val jsonPath = unicorn.jsonPath ?: "static/content/unicorns/$locale/${unicorn.title.lowercase().replace(" ", "_")}_unicorn.json"
                val jsonFile = File(jsonPath)

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
        get("/{locale}/unicorns") {
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
                UnicornsDao.read(driver)
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
                    val jsonFilePath = unicorn.jsonPath ?: "static/content/unicorns/$locale/${unicorn.title.lowercase().replace(" ", "_")}_unicorn.json"
                    val jsonFile = File(jsonFilePath)

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
        put("/{locale}/unicorns/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val locale = call.parameters["locale"] ?: "en"
            val unicorn = call.receive<Unicorn>()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            // Get existing unicorn to check if it exists and get its jsonPath
            val existingUnicorn = TenantDatabaseManager.withTenant(call) { driver ->
                UnicornsDao.read(driver, id)
            }

            if (existingUnicorn == null) {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
                return@put
            }

            // Generate JSON path if not already set
            val jsonPath = existingUnicorn.jsonPath ?: "static/content/unicorns/$locale/${unicorn.title.lowercase().replace(" ", "_")}_unicorn.json"
            val updatedUnicorn = unicorn.copy(id = id, jsonPath = jsonPath)

            // Update unicorn in database
            val success = TenantDatabaseManager.withTenant(call) { driver ->
                UnicornsDao.update(driver, updatedUnicorn)
            }

            if (!success) {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
                return@put
            }

            // Update JSON file
            try {
                val jsonDir = File("static/content/unicorns/$locale")
                jsonDir.mkdirs()

                val jsonContent = UnicornContent(
                    title = updatedUnicorn.title,
                    icon = updatedUnicorn.icon ?: "",
                    description = updatedUnicorn.description
                )

                File(jsonPath).writeText(Json.encodeToString(jsonContent))
            } catch (e: Exception) {
                // Log error but continue
                println("Error updating JSON file: ${e.message}")
            }

            call.respond(HttpStatusCode.OK, updatedUnicorn)
        }

        // Delete a unicorn
        delete("/{locale}/unicorns/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val locale = call.parameters["locale"] ?: "en"

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            // Get existing unicorn to check if it exists and get its jsonPath
            val existingUnicorn = TenantDatabaseManager.withTenant(call) { driver ->
                UnicornsDao.read(driver, id)
            }

            if (existingUnicorn == null) {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
                return@delete
            }

            // Delete unicorn from database
            val success = TenantDatabaseManager.withTenant(call) { driver ->
                UnicornsDao.delete(driver, id)
            }

            if (success) {
                // Delete JSON file
                try {
                    val jsonPath = existingUnicorn.jsonPath ?: "static/content/unicorns/$locale/${existingUnicorn.title.lowercase().replace(" ", "_")}_unicorn.json"
                    val jsonFile = File(jsonPath)
                    if (jsonFile.exists()) {
                        jsonFile.delete()
                    }
                } catch (e: Exception) {
                    // Log error but continue
                    println("Error deleting JSON file: ${e.message}")
                }

                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Unicorn not found")
            }
        }
    }
}