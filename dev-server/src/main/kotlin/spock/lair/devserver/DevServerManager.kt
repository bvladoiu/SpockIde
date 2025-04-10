// File: src/main/kotlin/spock/lair/managers/DevServerManager.kt (or similar path)
package spock.lair.devserver

import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Potentially Ktor client imports if proxying is done here
// import io.ktor.client.*
// import io.ktor.client.engine.cio.*
// import io.ktor.client.request.*

object DevServerManager {
    // Scope for background tasks like running a proxy
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("DevServerManagerScope"))
    private val mutex = Mutex() // To protect shared state if needed

    // --- State ---
    // Example: Track if a proxy is active and its target
    private var proxyJob: Job? = null
    private var proxyTargetUrl: String? = null

    // --- Public API ---

    /** Handles commands intended for the Dev Server or Proxy */
    suspend fun handle(commandString: String, session: DefaultWebSocketSession) {
        val parts = commandString.trim().split(" ", limit = 2)
        val action = parts.getOrNull(0)?.lowercase()
        val args = parts.getOrNull(1) ?: "" // Rest of the string

        println("DevServerManager handling: action='$action', args='$args'") // Logging

        mutex.withLock { // Lock if modifying state
            when (action) {
                "proxy_start" -> {
                    if (proxyJob?.isActive == true) {
                        session.send(Frame.Text("Proxy already running for $proxyTargetUrl"))
                        return@withLock
                    }
                    if (args.isBlank()) {
                        session.send(Frame.Text("Error: proxy_start requires a target URL"))
                        return@withLock
                    }
                    startProxy(args, session) // Call internal function
                }

                "proxy_stop" -> {
                    if (proxyJob?.isActive != true) {
                        session.send(Frame.Text("Proxy is not running."))
                        return@withLock
                    }
                    stopProxy(session) // Call internal function
                }

                "proxy_status" -> {
                    if (proxyJob?.isActive == true) {
                        session.send(Frame.Text("Proxy active for: $proxyTargetUrl"))
                    } else {
                        session.send(Frame.Text("Proxy stopped."))
                    }
                }
                // Add other dev server commands here: e.g., serve_static, reload_config
                "hello" -> session.send(Frame.Text("Hello from DevServerManager!"))
                else -> session.send(Frame.Text("DevServerManager: Unknown action '$action'. Try: proxy_start <url>, proxy_stop, proxy_status"))
            }
        }
    }

    /** Called on server shutdown to clean up resources */
    fun shutdown() {
        println("Shutting down DevServerManager...")
        scope.cancel() // Cancel all coroutines started by this manager
        println("DevServerManager shutdown complete.")
    }

    // --- Internal Logic ---

    private suspend fun startProxy(url: String, session: DefaultWebSocketSession) {
        println("Attempting to start proxy to: $url")
        proxyTargetUrl = url
        // Replace this with your actual proxy implementation
        proxyJob = scope.launch {
            try {
                session.send(Frame.Text("Proxy to $url starting... (IMPLEMENTATION PENDING)"))
                println("PROXY LOGIC FOR $url WOULD RUN HERE")
                // Example: Keep running until cancelled
                delay(Long.MAX_VALUE)
            } catch (e: CancellationException) {
                println("Proxy to $url cancelled.")
                // No need to send message here, stopProxy does it
            } catch (e: Exception) {
                println("Error in proxy task for $url: ${e.message}")
                runCatching { session.send(Frame.Text("Error in proxy for $url: ${e.message}")) }
            } finally {
                // Clean up state if this specific job instance ends
                mutex.withLock {
                    if (proxyTargetUrl == url) { // Check if it's still the same proxy task
                        proxyTargetUrl = null
                        proxyJob = null
                        println("Proxy state cleaned up for $url")
                    }
                }
            }
        }
        // Optional: Log completion
        proxyJob?.invokeOnCompletion { cause ->
            if (cause != null && cause !is CancellationException) {
                println("Proxy job ($url) completed with error: $cause")
            }
        }
        session.send(Frame.Text("Proxy started for: $url (Job: ${proxyJob != null})"))
        println("Proxy job launched for $url")
    }

    private suspend fun stopProxy(session: DefaultWebSocketSession) {
        val urlBeingStopped = proxyTargetUrl
        println("Attempting to stop proxy (was for $urlBeingStopped)...")
        proxyJob?.cancel("Proxy stop requested") // Cancel the job
        // State (proxyJob, proxyTargetUrl) is cleaned up in the job's finally block or immediately after cancel
        proxyJob = null
        proxyTargetUrl = null
        session.send(Frame.Text("Proxy stop requested (was for $urlBeingStopped)."))
        println("Proxy stop request sent.")
    }
}


object PlaywrightManager {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("PlaywrightManagerScope"))

    suspend fun handle(commandString: String, session: DefaultWebSocketSession) {
        val parts = commandString.trim().split(" ", limit = 2)
        val action = parts.getOrNull(0)?.lowercase()
        val args = parts.getOrNull(1) ?: ""

        println("PlaywrightManager handling: action='$action', args='$args'")

        when (action) {
            "run_script" -> {
                if (args.isBlank()) {
                    session.send(Frame.Text("Error: run_script requires a script name or arguments"))
                    return
                }
                runScript(args, session)
            }
            // Add other playwright commands: e.g., status, list_browsers
            "hello" -> session.send(Frame.Text("Hello from PlaywrightManager!"))
            else -> session.send(Frame.Text("PlaywrightManager: Unknown action '$action'. Try: run_script <script_args>"))
        }
    }

    fun shutdown() {
        println("Shutting down PlaywrightManager...")
        scope.cancel() // Cancel any running scripts/tasks
        println("PlaywrightManager shutdown complete.")
    }

    // --- Internal Logic ---

    private fun runScript(scriptArgs: String, session: DefaultWebSocketSession) {
        println("Attempting to run Playwright script with args: $scriptArgs")
        scope.launch {
            try {
                session.send(Frame.Text("Launching Playwright script: $scriptArgs ..."))
                // --- HERE: Call your Playwright logic ---
                // Option 1: Call shared Kotlin code (if Playwright logic is in a shared KMP module)
                // val result = SharedPlaywrightCode.execute(scriptArgs)
                // session.send(Frame.Text("Playwright Result: $result"))

                // Option 2: Launch external Node.js process
                val command = listOf("node", "path/to/your/playwright_runner.js", scriptArgs)
                println("Executing: ${command.joinToString(" ")}")
                val processBuilder = ProcessBuilder(command)
                processBuilder.redirectErrorStream(true) // Combine stdout/stderr
                val process = processBuilder.start()

                // Stream output back (consider more robust handling for production)
                process.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        println("Playwright Output: $line")
                        // Avoid flooding, maybe send only key lines or summaries
                        if (line.length < 200) { // Example filter
                            runBlocking { // Using runBlocking here is okay for brief sends within IO scope
                                runCatching { session.send(Frame.Text("PW> $line")) }
                            }
                        }
                    }
                }

                val exitCode = process.waitFor()
                println("Playwright script finished with exit code: $exitCode")
                session.send(Frame.Text("Playwright script '$scriptArgs' finished with exit code $exitCode."))

            } catch (e: Exception) {
                println("Error running Playwright script '$scriptArgs': ${e.message}")
                e.printStackTrace()
                runCatching { session.send(Frame.Text("Error running Playwright '$scriptArgs': ${e.message}")) }
            }
        }
        println("Playwright script launch initiated for: $scriptArgs")
    }
}