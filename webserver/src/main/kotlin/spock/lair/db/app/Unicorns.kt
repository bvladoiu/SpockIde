package spock.lair.db.app

import app.cash.sqldelight.db.SqlDriver
import spock.lair.db.app.AppDatabase

/**
 * DAO-like object for Unicorns with CRUD operations.
 * This class provides a DSL for working with Unicorn entities.
 */
object UnicornsDao {
    /**
     * Create a new unicorn in the database.
     */
    fun create(driver: SqlDriver, unicorn: Unicorn): Long {
        val database = AppDatabase(driver)

        // Insert the unicorn
        database.unicornsQueries.insert(
            title = unicorn.title,
            description = unicorn.description,
            index_order = unicorn.index_order.toLong(),
            jsonPath = unicorn.jsonPath
        )

        // Get the last inserted ID
        return database.unicornsQueries.lastInsertRowId().executeAsOne()
    }

    /**
     * Read all unicorns from the database.
     */
    fun read(driver: SqlDriver): List<Unicorn> {
        val database = AppDatabase(driver)

        // Get all unicorns
        return database.unicornsQueries.selectAll().executeAsList().map { dbUnicorn ->
            Unicorn(
                id = dbUnicorn.id.toInt(),
                title = dbUnicorn.title,
                description = dbUnicorn.description,
                index_order = dbUnicorn.index_order.toInt(),
                jsonPath = dbUnicorn.jsonPath
            )
        }
    }

    /**
     * Read a unicorn by ID from the database.
     */
    fun read(driver: SqlDriver, id: Int): Unicorn? {
        val database = AppDatabase(driver)

        // Get unicorn by ID
        val dbUnicorn = database.unicornsQueries.selectById(id.toLong()).executeAsOneOrNull() ?: return null

        return Unicorn(
            id = dbUnicorn.id.toInt(),
            title = dbUnicorn.title,
            description = dbUnicorn.description,
            index_order = dbUnicorn.index_order.toInt(),
            jsonPath = dbUnicorn.jsonPath
        )
    }

    /**
     * Update a unicorn in the database.
     */
    fun update(driver: SqlDriver, unicorn: Unicorn): Boolean {
        if (unicorn.id == null) return false

        val database = AppDatabase(driver)

        // Check if the unicorn exists
        val exists = database.unicornsQueries.selectById(unicorn.id.toLong()).executeAsOneOrNull() != null
        if (!exists) return false

        // Update the unicorn
        database.unicornsQueries.update(
            title = unicorn.title,
            description = unicorn.description,
            index_order = unicorn.index_order.toLong(),
            jsonPath = unicorn.jsonPath,
            id = unicorn.id.toLong()
        )

        return true
    }

    /**
     * Delete a unicorn from the database.
     */
    fun delete(driver: SqlDriver, id: Int): Boolean {
        val database = AppDatabase(driver)

        // Check if the unicorn exists
        val exists = database.unicornsQueries.selectById(id.toLong()).executeAsOneOrNull() != null
        if (!exists) return false

        // Delete the unicorn
        database.unicornsQueries.delete(id.toLong())

        return true
    }
}
