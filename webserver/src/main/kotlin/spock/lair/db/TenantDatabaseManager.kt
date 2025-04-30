package spock.lair.db

import app.cash.sqldelight.db.SqlDriver
import io.ktor.server.application.*
import io.ktor.server.request.*

/**
 * Manages database access for different tenants.
 * This class handles tenant identification from URL paths and provides access to the appropriate database.
 */
object TenantDatabaseManager {
    
    /**
     * Get the tenant from the current call.
     * This method extracts the tenant from the URL path.
     */
    fun getTenantFromCall(call: ApplicationCall): Tenant {
        val path = call.request.path()
        return Tenant.fromPath(path) ?: Tenant.COMMON
    }
    
    /**
     * Get a database driver for the tenant identified in the current call.
     */
    fun getDriverForCall(call: ApplicationCall): SqlDriver {
        val tenant = getTenantFromCall(call)
        return DatabaseFactory.getDriver(tenant)
    }
    
    /**
     * Get the common database driver.
     * This is a convenience method for accessing the common database.
     */
    fun getCommonDriver(): SqlDriver {
        return DatabaseFactory.getCommonDriver()
    }
    
    /**
     * Execute a database operation with the tenant identified in the current call.
     * This method provides a DSL for executing database operations.
     */
    inline fun <T> withTenant(call: ApplicationCall, block: (SqlDriver) -> T): T {
        val driver = getDriverForCall(call)
        return block(driver)
    }
    
    /**
     * Execute a database operation with the common database.
     * This method provides a DSL for executing database operations.
     */
    inline fun <T> withCommon(block: (SqlDriver) -> T): T {
        val driver = getCommonDriver()
        return block(driver)
    }
    
    /**
     * Execute a database operation with both the tenant-specific and common databases.
     * This method provides a DSL for executing database operations that need access to both databases.
     */
    inline fun <T> withBoth(call: ApplicationCall, block: (tenant: SqlDriver, common: SqlDriver) -> T): T {
        val tenantDriver = getDriverForCall(call)
        val commonDriver = getCommonDriver()
        return block(tenantDriver, commonDriver)
    }
    
    /**
     * Initialize the database system.
     * This method should be called when the application starts.
     */
    fun initialize() {
        // Initialize all tenant databases
        Tenant.values().forEach { DatabaseFactory.getDriver(it) }
    }
    
    /**
     * Close all database connections.
     * This method should be called when the application shuts down.
     */
    fun shutdown() {
        DatabaseFactory.closeAll()
    }
}