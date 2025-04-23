// file :web:jsMain:JsMain.kt
package spock.lair.ktor

import org.w3c.dom.asList

fun main(){
    kotlinx.browser.document.querySelectorAll("*").asList().forEach {
        print("Node: ${it.nodeName}")
        println(" type: ${it.nodeType}")
    }
}