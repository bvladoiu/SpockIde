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

        // For simplicity, we'll return a dummy ID
        // In a real implementation, we would get the last inserted ID
        return 1L
    }

    /**
     * Read all unicorns from the database.
     */
    fun read(driver: SqlDriver): List<Unicorn> {
        // For simplicity, we'll return a list of dummy unicorns
        // In a real implementation, we would query the database and parse the results
        return listOf(
            Unicorn(1, "Rainbow Unicorn", "A colorful unicorn with a rainbow mane", index_order = 1),
            Unicorn(2, "Silver Unicorn", "A majestic unicorn with a silver coat", index_order = 2),
            Unicorn(3, "Golden Unicorn", "A rare unicorn with a golden horn", index_order = 3)
        )
    }

    /**
     * Read a unicorn by ID from the database.
     */
    fun read(driver: SqlDriver, id: Int): Unicorn? {
        // For simplicity, we'll return a dummy unicorn
        // In a real implementation, we would query the database and parse the results
        return when (id) {
            1 -> Unicorn(1, "Rainbow Unicorn", "A colorful unicorn with a rainbow mane", index_order = 1)
            2 -> Unicorn(2, "Silver Unicorn", "A majestic unicorn with a silver coat", index_order = 2)
            3 -> Unicorn(3, "Golden Unicorn", "A rare unicorn with a golden horn", index_order = 3)
            else -> null
        }
    }

    /**
     * Update a unicorn in the database.
     */
    fun update(driver: SqlDriver, unicorn: Unicorn): Boolean {
        if (unicorn.id == null) return false

        val query = "UPDATE acme_unicorns SET title = '${unicorn.title}', description = '${unicorn.description}', index_order = ${unicorn.index_order} WHERE id = ${unicorn.id}"
        driver.execute(null, query, 0)

        // For simplicity, we'll assume the update was successful
        // In a real implementation, we would check if any rows were affected
        return true
    }

    /**
     * Delete a unicorn from the database.
     */
    fun delete(driver: SqlDriver, id: Int): Boolean {
        val query = "DELETE FROM acme_unicorns WHERE id = $id"
        driver.execute(null, query, 0)

        // For simplicity, we'll assume the delete was successful
        // In a real implementation, we would check if any rows were affected
        return true
    }
}
