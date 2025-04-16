package spock.lair.fileio

import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap // Use for basic thread safety of cache

/**
 * A simple persistent key-value store for String pairs backed by a local file.
 * Saves changes immediately on put/remove/clear operations.
 * File I/O operations are currently synchronous within the methods.
 * Wrap calls in appropriate dispatchers (e.g., Dispatchers.IO) if needed.
 *
 * @param filePath The path (absolute or relative) to the backing properties file.
 */
class PersistentMap(private val filePath: String) {

    private val file = File(filePath)
    // Use ConcurrentHashMap for thread-safe access to the in-memory cache
    private val cache = ConcurrentHashMap<String, String>()

    init {
        // Load existing data immediately when the instance is created
        loadFromFile()
        println("[PersistentMap] Initialized for file: $filePath. Loaded ${cache.size} items.")
    }

    /**
     * Retrieves the value for a given key from the in-memory cache.
     *
     * @param key The key string.
     * @return The associated value string, or null if the key is not found.
     */
    fun get(key: String): String? {
        return cache[key]
    }

    /**
     * Saves (associates) a value with a key.
     * The change is immediately persisted to the file.
     * If the key already exists, its value is overwritten.
     * Blank keys are ignored.
     *
     * @param key The key string (must not be blank).
     * @param value The value string.
     */
    fun save(key: String, value: String) {
        if (key.isBlank()) {
            logWarn("Attempted to save a blank key. Operation ignored.")
            return
        }
        cache[key] = value
        // Persist the entire map state after modification
        persistCacheToFile()
        // logInfo("Saved key '$key'.") // Optional: more verbose logging
    }

    /**
     * Removes the entry for the specified key.
     * The change is immediately persisted to the file if the key existed.
     *
     * @param key The key string to remove.
     * @return The value that was previously associated with the key, or null.
     */
    fun remove(key: String): String? {
        val removedValue = cache.remove(key)
        if (removedValue != null) {
            // Persist only if something was actually removed
            persistCacheToFile()
            // logInfo("Removed key '$key'.") // Optional
        }
        return removedValue
    }

    /**
     * Removes all entries from the map and clears the backing file.
     * The change is immediately persisted.
     */
    fun clearAll() {
        if (cache.isNotEmpty()) {
            cache.clear()
            persistCacheToFile() // Save the empty state
            logInfo("Cleared all entries.")
        }
    }

    /**
     * Returns an immutable snapshot (copy) of all key-value pairs currently in the map.
     */
    fun getAll(): Map<String, String> {
        return cache.toMap() // Creates an immutable copy
    }

    /**
     * Gets the number of key-value pairs currently stored.
     */
    val size: Int
        get() = cache.size

    /**
     * Checks if the map contains any entries.
     */
    fun isEmpty(): Boolean = cache.isEmpty()

    /**
     * Checks if the map contains the specified key.
     */
    fun containsKey(key: String): Boolean = cache.containsKey(key)


    // --- Private Persistence Logic ---

    /**
     * Loads data from the file into the cache upon initialization.
     * Handles basic file existence and read errors.
     */
    private fun loadFromFile() {
        ensureParentDirectoryExists()
        if (!file.exists() || !file.isFile) {
            logInfo("File not found ('$filePath'). Starting fresh.")
            cache.clear() // Ensure cache is empty
            return
        }

        try {
            val loadedData = mutableMapOf<String, String>()
            // Use useLines for safe resource handling
            file.useLines { lines ->
                lines.forEachIndexed { index, line ->
                    if (line.contains('=')) {
                        // limit = 2 ensures value can contain '='
                        val parts = line.split("=", limit = 2)
                        if (parts.size == 2 && parts[0].isNotBlank()) {
                            loadedData[parts[0]] = parts[1]
                        } else {
                            logWarn("Skipping malformed line #${index + 1} during load: '$line'")
                        }
                    } else if (line.isNotBlank()) {
                        logWarn("Skipping line #${index + 1} without '=' during load: '$line'")
                    }
                }
            }
            // Update cache (clear first for simplicity, could merge if needed)
            cache.clear()
            cache.putAll(loadedData)
        } catch (e: IOException) {
            logError("Failed to load from file '$filePath': ${e.message}", e)
            // Keep cache empty or preserve previous state? Let's clear on load error.
            cache.clear()
        } catch (e: Exception) { // Catch unexpected errors during loading
            logError("Unexpected error loading from file '$filePath': ${e.message}", e)
            cache.clear()
        }
    }

    /**
     * Writes the current state of the cache to the backing file.
     * Uses a temporary file and rename for basic atomicity.
     * This is a synchronous blocking operation.
     */
    private fun persistCacheToFile() {
        ensureParentDirectoryExists()
        val tempFilePath = file.absolutePath + ".tmp"
        val tempFile = File(tempFilePath)

        try {
            // Write to temporary file first
            tempFile.bufferedWriter().use { writer ->
                // Iterate over a stable snapshot for thread safety during write
                cache.toMap().entries.forEach { entry ->
                    writer.write("${entry.key}=${entry.value}")
                    writer.newLine()
                }
            }

            // Attempt atomic rename (preferred)
            // On Windows, renameTo might fail if the target file exists
            if (file.exists()) {
                if (!file.delete()){
                    logWarn("Could not delete original file for overwrite: $filePath")
                    // Consider alternative: tempFile.copyTo(file, true) if delete fails?
                    // For now we proceed hoping rename works or handles overwrite.
                }
            }

            if (!tempFile.renameTo(file)) {
                // Fallback if rename fails (e.g., different filesystems, permissions, Windows lock)
                logWarn("Atomic rename failed for '$filePath'. Attempting copy fallback.")
                tempFile.copyTo(file, overwrite = true)
                tempFile.delete() // Clean up temp file
            }
            // logInfo("Persisted ${cache.size} entries to $filePath") // Can be noisy
        } catch (e: IOException) {
            logError("Failed to save to file '$filePath': ${e.message}", e)
            // Attempt to clean up temp file on error
            if (tempFile.exists()) {
                tempFile.delete()
            }
            // Consider re-throwing or a different error strategy
        } catch (e: Exception) { // Catch unexpected errors during save
            logError("Unexpected error saving to file '$filePath': ${e.message}", e)
            if (tempFile.exists()) { tempFile.delete() }
        }
    }

    /** Ensures the parent directory for the target file exists. */
    private fun ensureParentDirectoryExists() {
        try {
            file.parentFile?.mkdirs()
        } catch (e: SecurityException) {
            logError("Permission denied creating directories for '$filePath'", e)
        }
    }


    // --- Internal Logging Helpers ---
    // Replace with a proper logger if needed
    private fun logInfo(message: String) = println("[PersistentMap INFO] $message")
    private fun logWarn(message: String) = println("[PersistentMap WARN] $message")
    private fun logError(message: String, error: Throwable? = null) {
        System.err.println("[PersistentMap ERROR] $message")
        error?.printStackTrace(System.err)
    }
}

fun main() {
    // --- Add this line ---
    val workingDir = System.getProperty("user.dir")
    println(">>> Current Working Directory: $workingDir")
    // --- End of added line ---

    println("--- PersistentMap Test ---")

    // The file path will now be relative to the printed working directory
    val testFilePath = "./my_test_map.txt"
    val testFile = File(testFilePath) // File path construction remains the same

    // Clean up any previous test file before starting
    if (testFile.exists()) {
        println("Deleting previous test file: $testFilePath")
        testFile.delete()
    }

    // ... (rest of your test code: create PersistentMap, save, print, cleanup) ...

    // 2. Create an instance of PersistentMap
    println("Creating PersistentMap instance for file: $testFilePath")
    val testMap = PersistentMap(testFilePath)

    // 3. Save 10 key-value pairs
    println("\nSaving 10 key-value pairs...")
    for (i in 1..10) {
        val key = "key_$i"
        val value = "This is value number $i"
        // println("  Saving: '$key' = '$value'") // Keep logging minimal if desired
        testMap.save(key, value)
    }
    println("Finished saving. Map size: ${testMap.size}")

    // 4. Retrieve and print all values
    println("\nRetrieving all entries from the map...")
    val allEntries = testMap.getAll()

    if (allEntries.isEmpty()) {
        println("The map is empty after attempting to save!")
    } else {
        println("Current entries (${allEntries.size}):")
        allEntries.forEach { (key, value) ->
            println("  - '$key': '$value'")
        }
    }

    // 5. Optional: Demonstrate retrieving a single value
    println("\nGetting a single value ('key_5'):")
    val singleValue = testMap.get("key_5")
    println("  Value for 'key_5': $singleValue")

    // 6. Optional: Clean up the test file afterwards
    println("\nCleaning up test file: $testFilePath")
    if (testFile.delete()) {
        println("Test file deleted successfully.")
    } else {
        println("Warning: Failed to delete test file.")
    }


    println("\n--- Test Finished ---")
}

// --- You still need the full PersistentMap class definition above this main function ---