// In :klient-js:jsMain:main.js.kt
package spock.lair

import kotlinx.coroutines.*

fun main() {
    MainScope().launch {
        bridge.collect {
            log(it)
        }
    }
    println("Browser JVM CLI Started (Minimal Setup - OK!)")
    println("Waiting indefinitely...")


}
