package spock.lair.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Factory for creating and managing database connections.
 * This class implements connection pooling for efficient database access.
 * Simplified for single-tenant approach.
 */
object DatabaseFactory {
    private val staticDir = File("static").apply { mkdirs() }
    private val databasesDir = File("static/db").apply { mkdirs() }
    private val versionDir = File(databasesDir, "versions").apply { mkdirs() }
    private val driverCache = ConcurrentHashMap<Tenant, SqlDriver>()
    private val migrators = ConcurrentHashMap<Tenant, DatabaseMigrator>()

    /**
     * Get a database driver for the specified tenant.
     * If the driver doesn't exist, it will be created and initialized.
     * Note: In the single-tenant approach, this always returns the driver for the ACME tenant.
     */
    fun getDriver(tenant: Tenant): SqlDriver {
        return driverCache.computeIfAbsent(tenant) { createDriver(it) }
    }

    /**
     * Create a new database driver for the specified tenant.
     * This method also handles database creation and migration.
     */
    private fun createDriver(tenant: Tenant): SqlDriver {
        // For ACME tenant, use the root static directory
        val dbFile = if (tenant == Tenant.ACME) {
            File(staticDir, tenant.dbName)
        } else {
            File(databasesDir, tenant.dbName)
        }
        val versionFile = File(versionDir, "${tenant.id}.version")
        val migrator = getMigrator(tenant)

        // Create JDBC SQLite driver
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

        // Check if the database exists
        val dbExists = dbFile.exists()

        if (!dbExists) {
            // Create the database
            migrator.onCreate(driver)
            // Save initial version
            saveVersion(versionFile, migrator.getCurrentVersion())
        } else {
            // Check if the database needs to be upgraded
            val currentVersion = migrator.getCurrentVersion()
            val userVersion = loadVersion(versionFile)

            if (userVersion < currentVersion) {
                migrator.onUpgrade(driver, userVersion, currentVersion)
                saveVersion(versionFile, currentVersion)
            }
        }

        return driver
    }

    /**
     * Get a database migrator for the specified tenant.
     */
    private fun getMigrator(tenant: Tenant): DatabaseMigrator {
        return migrators.computeIfAbsent(tenant) { DatabaseMigrator(it) }
    }

    /**
     * Load the database version from a file.
     */
    private fun loadVersion(versionFile: File): Int {
        return if (versionFile.exists()) {
            try {
                versionFile.readText().trim().toInt()
            } catch (e: Exception) {
                0
            }
        } else {
            0
        }
    }

    /**
     * Save the database version to a file.
     */
    private fun saveVersion(versionFile: File, version: Int) {
        versionFile.writeText(version.toString())
    }

    /**
     * Close all database connections.
     * This should be called when the application is shutting down.
     */
    fun closeAll() {
        driverCache.values.forEach { it.close() }
        driverCache.clear()
    }
}
