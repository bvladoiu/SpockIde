package spock.lair.db

import app.cash.sqldelight.db.SqlDriver
import io.ktor.server.application.*

/**
 * Manages database access for the application.
 * This is a simplified version that only supports a single database.
 */
object TenantDatabaseManager {

    /**
     * Get the database driver.
     */
    fun getDriver(): SqlDriver {
        return DatabaseFactory.getDriver(Tenant.ACME)
    }

    /**
     * Execute a database operation.
     * This method provides a DSL for executing database operations.
     */
    inline fun <T> withTenant(call: ApplicationCall, block: (SqlDriver) -> T): T {
        val driver = getDriver()
        return block(driver)
    }

    /**
     * Initialize the database system.
     * This method should be called when the application starts.
     */
    fun initialize() {
        // Initialize the database
        DatabaseFactory.getDriver(Tenant.ACME)
    }

    /**
     * Close all database connections.
     * This method should be called when the application shuts down.
     */
    fun shutdown() {
        DatabaseFactory.closeAll()
    }
}
