package spock.lair.tools.playwright

import com.microsoft.playwright.*
import com.microsoft.playwright.Browser
import kotlinx.coroutines.*
import spock.lair.strings.file
import spock.lair.strings.id
import spock.lair.strings.type

object Browser {
    var scope = CoroutineScope(Dispatchers. + SupervisorJob())
    var playwright = Playwright.create()
    var webkit: Browser = playwright.webkit().launch(BrowserType.LaunchOptions().setHeadless(false))
    var page: Page = webkit.newPage()

    fun cli(command: String) = scope.launch {
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

    fun draft(args: String) {

    }
}