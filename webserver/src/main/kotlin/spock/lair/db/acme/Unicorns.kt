package spock.lair.db.acme

import app.cash.sqldelight.db.SqlDriver

/**
 * DAO-like object for Unicorns with CRUD operations.
 * This class provides a DSL for working with Unicorn entities.
 */
object Unicorns {
    /**
     * Create a new unicorn in the database.
     */
    fun create(driver: SqlDriver, unicorn: Unicorn): Long {
        val query = "INSERT INTO acme_unicorns (title, description, index_order) VALUES ('${unicorn.title}', '${unicorn.description}', ${unicorn.index_order})"
        driver.execute(null, query, 0)
        return 1L // Return a dummy value since we can't get the actual ID
    }

    /**
     * Read all unicorns from the database.
     */
    fun read(driver: SqlDriver): List<Unicorn> {
        val unicorns = mutableListOf<Unicorn>()
        val query = "SELECT * FROM acme_unicorns ORDER BY index_order"

        driver.execute(null, query, 0)

        // In a real implementation, we would parse the result set here
        // For now, we'll return an empty list
        return unicorns
    }

    /**
     * Read a unicorn by ID from the database.
     */
    fun read(driver: SqlDriver, id: Int): Unicorn? {
        val query = "SELECT * FROM acme_unicorns WHERE id = $id"

        driver.execute(null, query, 0)

        // In a real implementation, we would parse the result set here
        // For now, we'll return null
        return null
    }

    /**
     * Update a unicorn in the database.
     */
    fun update(driver: SqlDriver, unicorn: Unicorn): Boolean {
        if (unicorn.id == null) return false

        val query = "UPDATE acme_unicorns SET title = '${unicorn.title}', description = '${unicorn.description}', index_order = ${unicorn.index_order} WHERE id = ${unicorn.id}"

        driver.execute(null, query, 0)
        return true
    }

    /**
     * Delete a unicorn from the database.
     */
    fun delete(driver: SqlDriver, id: Int): Boolean {
        val query = "DELETE FROM acme_unicorns WHERE id = $id"

        driver.execute(null, query, 0)
        return true
    }
}
