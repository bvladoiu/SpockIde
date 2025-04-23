package spock.lair.tools.playwright

import com.microsoft.playwright.*
import com.microsoft.playwright.Browser
import kotlinx.coroutines.*
import spock.lair.strings.file
import spock.lair.strings.id
import spock.lair.strings.type

object Browser {
    var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    var playwright = Playwright.create()
    var webkit: Browser = playwright.webkit().launch(BrowserType.LaunchOptions().setHeadless(false))
    var page: Page = webkit.newPage()

    // DSL-style command execution
    operator fun invoke(command: String) = scope.launch {
        println(command)
        when (command.id()) {
            //install intrinsic scripts
            //setproxy
            "goto" -> page.navigate("https://www.marqeta.com/platform/applications")
            "reload" -> page.reload()
            "script" -> page.evaluate(command.file())
            "eval" ->  page.evaluate(command.file())
                else -> println("spock.lair.tools.playwright.BrowserCli: unknown: $command")
        }
    }

    // DSL-style navigation
    fun goto(url: String) = scope.launch {
        page.navigate(url)
    }

    // DSL-style page reload
    fun reload() = scope.launch {
        page.reload()
    }

    // DSL-style script evaluation
    fun script(scriptContent: String) = scope.launch {
        page.evaluate(scriptContent)
    }

    // DSL-style draft function
    fun draft(args: String) {
        // Implementation to be added
    }
}
