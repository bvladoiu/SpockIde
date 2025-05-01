# Tasks

## Fixed GraalVM Plugin Compilation Error - 08/15/2024
Completely removed the GraalVM plugin from the webserver module due to persistent compilation errors. The plugin was causing conflicts with the classpath. The GraalVM dependencies are still available for use with the custom native image build task.

## Added GraalVM Native Image Build - 08/15/2024
Added a custom Gradle task to build native executables using GraalVM and created comprehensive documentation in graalvm_native_build.md. The task automatically generates necessary configuration files and builds a standalone native executable that runs without a JVM.

## Added GraalVM and kotlinx serialization - 08/15/2024
Added GraalVM SDK and Native Image dependencies to the webserver module using version catalog. Kotlinx serialization was already configured but ensured it's properly set up in the webserver module.

## Purged project of Unicorns
Deleted the apis package for clean startover

## Removed SQLDelight and Database Logic - 07/25/2024
Purged the project of SQLDelight code and database logic. Replaced database-backed unicorns API with an in-memory implementation using a hardcoded list. Removed SQLDelight plugin and related libraries from the build configuration. Commented out database-related Gradle tasks. Updated DatabasePlugin to be a placeholder that doesn't initialize any database.

## Added multilingual navigation - 05/15/2023
Created language-specific directories (en/de) in static/components/data with pages.json files containing navigation items in English and German.

## Added JS copy task - 05/01/2025
Added a new task in the webserver module that copies JS output from web module to resources/js/ directory and made the run task depend on it.
