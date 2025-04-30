package spock.lair.db

import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import io.ktor.util.*

/**
 * Ktor plugin for the multitenant database system.
 * This plugin handles initialization and shutdown of the database system.
 */
class DatabasePlugin {
    /**
     * Configuration for the database plugin.
     */
    class Configuration {
        // Add configuration options here if needed
    }
    
    /**
     * Companion object for the plugin.
     */
    companion object Plugin : BaseApplicationPlugin<Application, Configuration, DatabasePlugin> {
        override val key = AttributeKey<DatabasePlugin>("DatabasePlugin")
        
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): DatabasePlugin {
            val configuration = Configuration().apply(configure)
            val plugin = DatabasePlugin()
            
            // Initialize the database system
            TenantDatabaseManager.initialize()
            
            // Register shutdown hook
            pipeline.environment.monitor.subscribe(ApplicationStopping) {
                TenantDatabaseManager.shutdown()
            }
            
            return plugin
        }
    }
}

/**
 * Extension function to configure the database plugin.
 */
fun Application.configureDatabases() {
    install(DatabasePlugin) {
        // Configure the plugin here if needed
    }
}