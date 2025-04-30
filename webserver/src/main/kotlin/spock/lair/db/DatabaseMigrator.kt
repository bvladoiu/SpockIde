package spock.lair.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

/**
 * Handles database migrations using a switch-based approach similar to Android's SQLiteOpenHelper.
 * This class provides onCreate and onUpgrade methods for the single database.
 * Simplified for single-tenant approach.
 */
class DatabaseMigrator(private val tenant: Tenant) {

    companion object {
        // Current database version
        private const val DATABASE_VERSION = 1
    }

    /**
     * Get the current version of the database.
     */
    fun getCurrentVersion(): Int {
        return DATABASE_VERSION
    }

    /**
     * Called when the database is created for the first time.
     * This method creates all the necessary tables.
     */
    fun onCreate(driver: SqlDriver) {
        createDatabase(driver)
    }

    /**
     * Called when the database needs to be upgraded from an older version to a newer one.
     * This method handles all the necessary schema changes.
     */
    fun onUpgrade(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        if (oldVersion >= newVersion) return
        upgradeDatabase(driver, oldVersion, newVersion)
    }

    // Database creation and upgrade
    private fun createDatabase(driver: SqlDriver) {
        // Create database tables
        driver.execute(null, """
            CREATE TABLE IF NOT EXISTS unicorns (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                index_order INTEGER NOT NULL,
                jsonPath TEXT
            )
        """.trimIndent(), 0)
    }

    private fun upgradeDatabase(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        // Use a switch-like approach without breaks
        var version = oldVersion

        if (version == 1) {
            // Upgrade from version 1 to 2
            // Add future migrations here
            version = 2
        }

        if (version == 2) {
            // Upgrade from version 2 to 3
            // Add future migrations here
            version = 3
        }

        // Add more version checks as needed
    }
}
