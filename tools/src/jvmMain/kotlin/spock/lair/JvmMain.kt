// :tools:src:jvmMain:JvmMain.kt
package spock.lair

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.ConsoleMessage
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import spock.lair.strings.type
import spock.lair.tools.shortcuts.Shortcuts
import java.nio.file.Path

fun main(){
    run()
}
fun run(args: String? = null) {
    val playwright = Playwright.create()
    val browser = playwright.chromium().launch(
        BrowserType.LaunchOptions().setHeadless(false)
            .setArgs(listOf("--disable-web-security", "--disable-features=IsolateOrigins,site-per-process"))
    )
    val context = browser.newContext()
    /*Shortcuts.on(NativeKeyEvent.VC_D) {
        context.pages().forEach {
            loadScript(it)
        }
    }*/
    val page = context.newPage()
    page.onConsoleMessage { message ->
        trace(message)
    }
    val scriptPath = "build/kotlin-webpack/js/productionExecutable/tools.js"
    val scriptContent = java.io.File(scriptPath).readText()
    page.addInitScript(scriptContent)

    page.navigate(args?.type() ?:"https://relay.material.io/")
    //page.waitForLoadState(LoadState.DOMCONTENTLOADED)
    //println("DOM Content Loaded")

    page.waitForLoadState(LoadState.LOAD)
    println("Page Fully Loaded")

    page.waitForLoadState(LoadState.NETWORKIDLE)
    println("Network Idle")
    //loadScript(page)
    //page.evaluate("inspect()")
}

fun trace(message: ConsoleMessage){
    val type = message.type().uppercase() // e.g., LOG, ERROR, WARNING
    val text = try {
        // Attempt to reconstruct the message from arguments for better formatting
        if (message.args().isNotEmpty()) {
            message.args().joinToString(" ") { arg ->
                try {
                    // Convert JSHandle to its JSON representation (best effort)
                    arg.jsonValue()?.toString() ?: "[JSHandle]"
                } catch (e: Exception) {
                    // Handle cases where jsonValue might fail
                    "[Error converting arg: ${e.message}]"
                }
            }
        } else {
            message.text() // Use the raw text if no arguments
        }
    } catch (e: Exception) {
        "[Error getting message text: ${e.message}]" // Fallback
    }

    val location = message.location() // Get source location (URL, line, column)

    // Print formatted message to JVM standard output
    println("[Browser $type @ $location]: $text")
}
fun loadScript(page: Page) {
    try {
        val options = Page.AddScriptTagOptions()
        options.setPath(Path.of("build/compileSync/js/main/productionExecutable/kotlin/lair-tools.js"))
        page.addScriptTag(options)
        println("✅ Script injected: ${page.url()}")
    } catch (e: Exception) {
        println("❌ Failed to inject script into: ${page.url()}")
        e.printStackTrace()
    }
}