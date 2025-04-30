package spock.lair.db

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

/**
 * Initializes SQLite database file in the specified static directory.
 * This class is used by the Gradle task to create and initialize database schema.
 * 
 * Note: Using raw SQL instead of SQLDelight Schema.create to avoid redeclaration issues
 * with the generated code.
 */
object DbInitializer {
    /**
     * Main function to initialize database file.
     * @param args Command line arguments. The first argument should be the path to the static directory.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            println("Error: Static directory path is required as the first argument")
            System.exit(1)
        }

        val staticDir = File(args[0])
        if (!staticDir.exists()) {
            staticDir.mkdirs()
        }

        println("Initializing database in: ${staticDir.absolutePath}")

        // Initialize the database
        initializeDatabase(staticDir, "app.db") { driver ->
            try {
                // Create app database tables using raw SQL
                driver.execute(null, """
                    CREATE TABLE IF NOT EXISTS unicorns (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        index_order INTEGER NOT NULL,
                        jsonPath TEXT
                    )
                """.trimIndent(), 0)
                println("Created app.db schema successfully")
            } catch (e: Exception) {
                println("Note: app.db schema may already exist: ${e.message}")
            }
        }

        println("Database initialization completed")
    }

    /**
     * Initialize the database file.
     * @param staticDir The directory where the database file should be created
     * @param dbName The name of the database file
     * @param initSchema A function that initializes the database schema
     */
    private fun initializeDatabase(staticDir: File, dbName: String, initSchema: (JdbcSqliteDriver) -> Unit) {
        val dbFile = File(staticDir, dbName)
        val jdbcUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        println("Initializing database: $dbName at $jdbcUrl")

        JdbcSqliteDriver(jdbcUrl).use { driver ->
            initSchema(driver)
        }
    }
}
