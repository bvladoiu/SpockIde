package spock.lair.tools.playwright

import com.microsoft.playwright.*
import kotlinx.coroutines.*
import spock.lair.strings.file
import spock.lair.strings.name
import spock.lair.strings.type

object BrowserCli {
    var scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    var playwright = Playwright.create()
    var webkit: Browser = playwright.webkit().launch(BrowserType.LaunchOptions().setHeadless(false))
    var page: Page = webkit.newPage()

    fun run(command: String) = scope.launch {
        when (command.type()) {
            //install intrinsic scripts
            //setproxy
            "goto" -> page.navigate(command.name())
            "reload" -> page.reload()
            "script" -> page.evaluate(command.file())
                else -> println("spock.lair.tools.playwright.BrowserCli: unknown: $command")
        }
    }

    fun draft(args: String) {

    }
}