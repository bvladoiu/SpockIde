package spock.lair.webapp

import kotlinx.browser.document

fun main() {
    console.log("Webapp module initialized")
    
    // Initialize the webapp functionality
    initWebapp()
}

/**
 * Initialize the webapp functionality
 */
fun initWebapp() {
    // Add your webapp initialization code here
    document.addEventListener("DOMContentLoaded", {
        console.log("DOM fully loaded and parsed from webapp module")
    })
}