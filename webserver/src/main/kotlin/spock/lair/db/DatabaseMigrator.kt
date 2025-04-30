package spock.lair.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

/**
 * Handles database migrations using a switch-based approach similar to Android's SQLiteOpenHelper.
 * This class provides onCreate and onUpgrade methods for each tenant database.
 */
class DatabaseMigrator(private val tenant: Tenant) {

    companion object {
        // Current database versions for each tenant
        private val DATABASE_VERSIONS = mapOf(
            Tenant.COMMON to 1,
            Tenant.CONTADEAL to 1,
            Tenant.PRISMA to 1
        )
    }

    /**
     * Get the current version of the database for the specified tenant.
     */
    fun getCurrentVersion(): Int {
        return DATABASE_VERSIONS[tenant] ?: 1
    }

    /**
     * Called when the database is created for the first time.
     * This method should create all the necessary tables.
     */
    fun onCreate(driver: SqlDriver) {
        when (tenant) {
            Tenant.COMMON -> createCommonDatabase(driver)
            Tenant.CONTADEAL -> createContadealDatabase(driver)
            Tenant.PRISMA -> createPrismaDatabase(driver)
        }
    }

    /**
     * Called when the database needs to be upgraded from an older version to a newer one.
     * This method should handle all the necessary schema changes.
     */
    fun onUpgrade(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        if (oldVersion >= newVersion) return

        when (tenant) {
            Tenant.COMMON -> upgradeCommonDatabase(driver, oldVersion, newVersion)
            Tenant.CONTADEAL -> upgradeContadealDatabase(driver, oldVersion, newVersion)
            Tenant.PRISMA -> upgradePrismaDatabase(driver, oldVersion, newVersion)
        }
    }

    // Common database creation and upgrade
    private fun createCommonDatabase(driver: SqlDriver) {
        // Create common database tables
        driver.execute(null, """
            CREATE TABLE IF NOT EXISTS settings (
                key TEXT PRIMARY KEY NOT NULL,
                value TEXT NOT NULL
            )
        """.trimIndent(), 0)
    }

    private fun upgradeCommonDatabase(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        // Use a switch-like approach without breaks
        var version = oldVersion

        if (version == 1) {
            // Upgrade from version 1 to 2
            // driver.execute(null, "ALTER TABLE settings ADD COLUMN description TEXT", 0)
            version = 2
        }

        if (version == 2) {
            // Upgrade from version 2 to 3
            // Add future migrations here
            version = 3
        }

        // Add more version checks as needed
    }

    // Contadeal database creation and upgrade
    private fun createContadealDatabase(driver: SqlDriver) {
        // Create contadeal database tables
        driver.execute(null, """
            CREATE TABLE IF NOT EXISTS contadeal_users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                email TEXT NOT NULL,
                created_at INTEGER NOT NULL
            )
        """.trimIndent(), 0)
    }

    private fun upgradeContadealDatabase(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        // Use a switch-like approach without breaks
        var version = oldVersion

        if (version == 1) {
            // Upgrade from version 1 to 2
            // driver.execute(null, "ALTER TABLE contadeal_users ADD COLUMN last_login INTEGER", 0)
            version = 2
        }

        if (version == 2) {
            // Upgrade from version 2 to 3
            // Add future migrations here
            version = 3
        }

        // Add more version checks as needed
    }

    // Prisma database creation and upgrade
    private fun createPrismaDatabase(driver: SqlDriver) {
        // Create prisma database tables
        driver.execute(null, """
            CREATE TABLE IF NOT EXISTS prisma_competences (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                competence_id TEXT NOT NULL UNIQUE,
                index_order INTEGER NOT NULL
            )
        """.trimIndent(), 0)
    }

    private fun upgradePrismaDatabase(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        // Use a switch-like approach without breaks
        var version = oldVersion

        if (version == 1) {
            // Upgrade from version 1 to 2
            // If we're upgrading from a version with prisma_products, drop it and create prisma_competences
            driver.execute(null, "DROP TABLE IF EXISTS prisma_products", 0)
            driver.execute(null, """
                CREATE TABLE IF NOT EXISTS prisma_competences (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    competence_id TEXT NOT NULL UNIQUE,
                    index_order INTEGER NOT NULL
                )
            """.trimIndent(), 0)
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
