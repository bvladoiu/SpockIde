// In :browser:jsMain:main.kt
package spock.lair



fun log(arg: String) = println(arg)


/*


val playwright = js("require('playwright')")

val launchBrowser = js(
    """
        async () => {
            const browser = await playwright.chromium.launch();
            return browser;
        }
        """
)

var browser: dynamic = null



fun main() {
    println("$NODE::started")
    Klient.start()
    browser = launchBrowser.asDynamic().invoke().await()
    MainScope().launch {
        bridge.collect { command ->
            println("command: $command, type: ${command.type()}, id: ${command.id()}")
            try {
                log("${command.type() == NODE}")
                if (command.type() == NODE)
                    launch { handleCommand(command.id()) }
            } catch (e: Exception) {
                val error = e.asDynamic()
                log("$NODE: Error'$command': ${error.message}\nStack: ${error.stack}")
            }
        }
    }
}


fun handleCommand(command: String) {
    // "FS_WATCH:/path/to/watch"
    // "JSDOM_PROCESS:<html string>"
    // "PW_LAUNCH"
    // "PW_GOTO:http://example.com"
    // "PW_EVAL:console.log('hello')"
    // "PW_CLOSE"
    // "PW_GET_CONSOLE" // Maybe to fetch recent history? (Requires custom logic)
    println("$NODE::handleCommand: $command playwright: $playwright, browser: $browser")
    when {
        command.startsWith("FS_WATCH:") -> {
            //val path = command.removePrefix("FS_WATCH:").trim()
            //log("TODO: Implement FS Watch for $path")
        }

        command.startsWith("FETCH_HTML:") -> {
            log("$NODE: JSDOM processing FETCH_HTML")
            */
/*    val html = command.removePrefix("JSDOM_PROCESS:").trim()
                log("Processing HTML with JSDOM...")
                val jsdomDoc = processHtmlWithJsDom(html)
                val title = jsdomDoc.title
                log("JSDOM processing complete. Title: $title")*//*

            // bridge.emit("JSDOM_RESULT:Title=$title") // Example reporting back
        }

        command == "START_BROWSER" -> {
            println("$NODE::START_BROWSER playwright: $playwright, browser: $browser")
        }

        command == "BROWSER_EVAL" -> {
            println("$NODE::BROWSER_EVAL")
        }

        else -> println("Unknown command received: $command")
    }
}*/
