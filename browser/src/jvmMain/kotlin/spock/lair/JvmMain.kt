// :browser:src:jvmMain:JvmMain.kt
package spock.lair

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.ConsoleMessage
import com.microsoft.playwright.Playwright
import spock.lair.strings.type

fun main() {
    run()
}

fun run(args: String? = null) {
    val playwright = Playwright.create()
    val browser = playwright.chromium().launch(
        BrowserType.LaunchOptions().setHeadless(false)
            .setArgs(listOf("--disable-web-security", "--disable-features=IsolateOrigins,site-per-process"))
    )
    val context = browser.newContext()
    val page = context.newPage()
    page.onConsoleMessage { message ->
        trace(message)
    }

    val scriptPath = "/build/kotlin-webpack/js/productionExecutable/browser.js"
    val projectRoot = System.getProperty("user.dir")
    val absoluteScriptPath = java.io.File(projectRoot, scriptPath)

    if (!absoluteScriptPath.exists()) {
        println("Error: Could not find script file at $scriptPath or $absoluteScriptPath")

        val staticDir = java.io.File(projectRoot, "static")
        if (staticDir.exists() && staticDir.isDirectory) {
            println("Files in static directory:")
            staticDir.listFiles()?.forEach { println("  - ${it.name}") }
        } else {
            println("Static directory not found at: ${staticDir.absolutePath}")
        }
        return
    }
    val scriptContent = absoluteScriptPath.readText()
    println("Script content length: ${scriptContent.length} characters")
    page.addInitScript(scriptContent)
    println("Script injected successfully")
    page.navigate(args?.type() ?: "https://relay.material.io/")
}

fun trace(message: ConsoleMessage) {
    val type = message.type().uppercase()
    val text = try {
        if (message.args().isNotEmpty()) {
            message.args().joinToString(" ") { arg ->
                try {
                    arg.jsonValue()?.toString() ?: "[JSHandle]"
                } catch (e: Exception) {
                    "[Error converting arg: ${e.message}]"
                }
            }
        } else {
            message.text()
        }
    } catch (e: Exception) {
        "[Error getting message text: ${e.message}]"
    }
    val location = message.location()
    println("[Browser $type @ $location]: $text")
}
