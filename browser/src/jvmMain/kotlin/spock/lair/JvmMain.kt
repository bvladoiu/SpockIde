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
    val scriptPath = "static/web.js"
    val scriptContent = java.io.File(scriptPath).readText()
    page.addInitScript(scriptContent)
    page.navigate(args?.type() ?: "https://relay.material.io/")
}

fun trace(message: ConsoleMessage) {
    val type = message.type().uppercase() // e.g., LOG, ERROR, WARNING
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
        "[Error getting message text: ${e.message}]" // Fallback
    }
    val location = message.location()
    println("[Browser $type @ $location]: $text")
}