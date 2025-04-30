//lair(root):built.gradle.kts

// Task name constants
val dependencyUpdatesTask = "dependencyUpdates"
val copyJsStaticTask = "copyJsStaticDev"
val runWebserverTask = "runWebserver"
val runBrowserTask = "runBrowser"
val initializeDatabasesTask = "initializeDatabases"
val fixDatabasesTask = "fixDatabases"
val directInitDatabasesTask = "directInitDatabases"
val buildWebServerTask = "buildWebServer"
val webJsBrowserProductionWebpackTask = ":web:jsBrowserProductionWebpack"
// No webapp project exists, so this task is removed
val webserverRunTask = ":webserver:run"
val browserJvmRunTask = ":browser:jvmRun"
val webserverClassesTask = ":webserver:classes"
val webserverCleanTask = ":webserver:clean"

plugins {
    alias(libs.plugins.benmanes.versions)
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.nodeGradle) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
}


tasks.named(dependencyUpdatesTask).configure {

}

val jsStaticDevDir = layout.buildDirectory.dir("../static")

tasks.register(copyJsStaticTask, DefaultTask::class) {
    group = "build"
    description = "Builds the JS bundles and copies output to Ktor's static serving directory."
    dependsOn(webJsBrowserProductionWebpackTask)

    doLast {
        val webJsDevBuildDir = project(":web").layout.buildDirectory.get().asFile.resolve("kotlin-webpack/js/productionExecutable")
        val staticDir = rootDir.resolve("static")
        staticDir.mkdirs()

        // Copy web.js files
        copy {
            from(webJsDevBuildDir)
            into(staticDir)
            include("*.js")
            include("*.js.map")
        }

        println("Copied JS dev files to: ${staticDir.absolutePath}")
    }
}

tasks.register(runWebserverTask, DefaultTask::class) {
    group = "application"
    description = "Runs the :webserver in development mode, serving static JS from the dev directory."
    dependsOn(buildWebServerTask)
    val ktorRunTask = tasks.getByPath(webserverRunTask)
    (ktorRunTask as JavaExec).apply{
        jvmArgs = listOf(
            "-Dio.ktor.development=${project.hasProperty("development") || project.gradle.startParameter.taskNames.any { it.contains(runWebserverTask) } }",
        )
        workingDir = rootDir
    }
    dependsOn(ktorRunTask)
}

tasks.register(runBrowserTask, DefaultTask::class) {
    group = "application"
    description = "Runs the browser in development mode, using the same JS bundle as the webserver."
    dependsOn(copyJsStaticTask)
    val browserRunTask = tasks.getByPath(browserJvmRunTask)
    (browserRunTask as JavaExec).apply {
        workingDir = rootDir
    }
    dependsOn(browserRunTask)
}

tasks.register(initializeDatabasesTask, JavaExec::class) {
    group = "database"
    description = "Initializes SQLite database files in the static directory"

    // Ensure the webserver code is compiled first
    dependsOn(webserverClassesTask)

    // Configure the JavaExec task
    doFirst {
        // Get the runtime classpath from the webserver project
        classpath = project(":webserver").tasks.named("classes").get().outputs.files +
                    project(":webserver").configurations.getByName("runtimeClasspath")
    }

    mainClass.set("spock.lair.db.DbInitializerKt")

    // Pass the path to the static directory as an argument
    args = listOf(rootProject.layout.projectDirectory.dir("static").asFile.absolutePath)

    // Set the working directory to the project root
    workingDir = rootDir
}

// Task to fix database issues by cleaning and reinitializing
tasks.register(fixDatabasesTask, JavaExec::class) {
    group = "database"
    description = "Fixes database issues by cleaning and reinitializing"

    // First clean the webserver module to remove all generated code
    dependsOn(webserverCleanTask)

    // Then ensure the webserver code is compiled
    dependsOn(webserverClassesTask)

    // Configure the JavaExec task
    doFirst {
        // Get the runtime classpath from the webserver project
        classpath = project(":webserver").tasks.named("classes").get().outputs.files +
                    project(":webserver").configurations.getByName("runtimeClasspath")
    }

    mainClass.set("spock.lair.db.DbInitializerKt")

    // Pass the path to the static directory as an argument
    args = listOf(rootProject.layout.projectDirectory.dir("static").asFile.absolutePath)

    // Set the working directory to the project root
    workingDir = rootDir

    doLast {
        println("Database initialization completed successfully")
    }
}

// Task to directly initialize databases without SQLDelight code generation
tasks.register(buildWebServerTask, DefaultTask::class) {
    group = "build"
    description = "Builds the web server and ensures the database exists without wiping data"

    dependsOn(copyJsStaticTask)
    dependsOn(webserverClassesTask)

    doLast {
        val staticDir = rootDir.resolve("static")
        val appDbFile = staticDir.resolve("app.db")

        if (!appDbFile.exists()) {
            // Only initialize the database if it doesn't exist
            println("Database file ${appDbFile.absolutePath} does not exist. Initializing...")
            tasks.getByName(directInitDatabasesTask).actions.forEach { it.execute(this) }
        } else {
            println("Database file ${appDbFile.absolutePath} already exists. Skipping initialization.")
        }
    }
}

tasks.register(directInitDatabasesTask, JavaExec::class) {
    group = "database"
    description = "Directly initializes SQLite database files in the static directory without SQLDelight code generation"

    // Configure the JavaExec task
    doFirst {
        // Get all required dependencies for database initialization
        val requiredDeps = project(":webserver").configurations.getByName("runtimeClasspath")
            .filter { it.name.contains("sqlite-jdbc") || 
                      it.name.contains("sqldelight-jdbc-driver") || 
                      it.name.contains("slf4j") }

        classpath = files(requiredDeps)

        // Create a temporary Java file that initializes the databases
        val tempDir = layout.buildDirectory.get().asFile.resolve("temp/directInit")
        tempDir.mkdirs()

        val tempJavaFile = File(tempDir, "DirectDbInitializer.java")
        tempJavaFile.writeText("""
            import java.io.File;
            import java.sql.Connection;
            import java.sql.DriverManager;
            import java.sql.Statement;

            public class DirectDbInitializer {
                public static void main(String[] args) throws Exception {
                    if (args.length == 0) {
                        System.err.println("Error: Static directory path is required as the first argument");
                        System.exit(1);
                    }

                    // Load the SQLite JDBC driver
                    try {
                        Class.forName("org.sqlite.JDBC");
                        System.out.println("SQLite JDBC driver loaded successfully");
                    } catch (ClassNotFoundException e) {
                        System.err.println("Error: SQLite JDBC driver not found: " + e.getMessage());
                        System.exit(1);
                    }

                    File staticDir = new File(args[0]);
                    if (!staticDir.exists()) {
                        staticDir.mkdirs();
                    }

                    System.out.println("Initializing databases in: " + staticDir.getAbsolutePath());

                    // Initialize app.db
                    initializeDatabase(staticDir, "app.db", 
                        "CREATE TABLE IF NOT EXISTS unicorns (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT NOT NULL," +
                        "description TEXT NOT NULL," +
                        "index_order INTEGER NOT NULL," +
                        "jsonPath TEXT" +
                        ")");

                    // Initialize common.db
                    initializeDatabase(staticDir, "common.db", 
                        "CREATE TABLE IF NOT EXISTS settings (" +
                        "key TEXT PRIMARY KEY NOT NULL," +
                        "value TEXT NOT NULL" +
                        ")");

                    // Initialize contadeal.db
                    initializeDatabase(staticDir, "contadeal.db", 
                        "CREATE TABLE IF NOT EXISTS contadeal_users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "email TEXT NOT NULL," +
                        "created_at INTEGER NOT NULL" +
                        ")");

                    // Initialize prisma.db
                    initializeDatabase(staticDir, "prisma.db", 
                        "CREATE TABLE IF NOT EXISTS prisma_competences (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "competence_id TEXT NOT NULL UNIQUE," +
                        "index_order INTEGER NOT NULL" +
                        ")");

                    System.out.println("Database initialization completed successfully");
                }

                private static void initializeDatabase(File staticDir, String dbName, String createTableSql) {
                    File dbFile = new File(staticDir, dbName);
                    String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

                    System.out.println("Initializing database: " + dbName + " at " + jdbcUrl);

                    try (Connection conn = DriverManager.getConnection(jdbcUrl);
                         Statement stmt = conn.createStatement()) {
                        stmt.execute(createTableSql);
                        System.out.println("Created " + dbName + " schema successfully");
                    } catch (Exception e) {
                        System.out.println("Note: " + dbName + " schema may already exist: " + e.getMessage());
                    }
                }
            }
        """.trimIndent())

        // Compile the Java file
        val javacResult = exec {
            workingDir = tempDir
            commandLine = listOf(
                "javac",
                "-cp", requiredDeps.asPath,
                tempJavaFile.absolutePath
            )
            isIgnoreExitValue = true
        }

        if (javacResult.exitValue != 0) {
            throw GradleException("Failed to compile DirectDbInitializer.java")
        }

        // Add the compiled class to the classpath
        classpath = files(tempDir) + classpath

        // Set the main class
        mainClass.set("DirectDbInitializer")

        // Pass the path to the static directory as an argument
        args = listOf(rootProject.layout.projectDirectory.dir("static").asFile.absolutePath)
    }

    // Set the working directory to the project root
    workingDir = rootDir
}
