package spock.lair

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import org.w3c.dom.asList
import org.w3c.fetch.RequestInit


fun savePage() {
    val pageContent = document.getElementById("page-content") ?: return
    val content = pageContent.innerHTML

    val tempDiv = document.createElement("div")
    tempDiv.innerHTML = content

    val buttons = tempDiv.querySelectorAll(".add-button, .remove-button").asList()
    for (button in buttons) {
        (button as Element).remove()
    }

    val controlIcons = tempDiv.querySelectorAll("div[data-control-icons='true']").asList()
    for (icon in controlIcons) {
        (icon as Element).remove()
    }

    val editableElements = tempDiv.querySelectorAll("[contenteditable='true']").asList()
    for (element in editableElements) {
        (element as Element).removeAttribute("contenteditable")
        element.removeAttribute("style")
    }

    val cleanContent = tempDiv.innerHTML

    window.fetch(
        "/api/save/home", RequestInit(
            method = "POST",
            body = cleanContent
        )
    ).then { response ->
        if (response.ok) {
            console.log("Page saved successfully")
        } else {
            console.error("Failed to save page")
        }
    }
}

fun loadPage() {
    window.fetch("/api/load/home").then { response ->
        if (response.ok) {
            response.text().then { content ->
                val pageContent = document.getElementById("page-content")
                if (pageContent != null) {
                    pageContent.innerHTML = content
                }
                console.log("Page loaded successfully")
            }
        } else {
            console.error("Failed to load page")
        }
    }
}

fun deployPage() {
    window.fetch(
        "/api/deploy/home", RequestInit(
            method = "POST"
        )
    ).then { response ->
        if (response.ok) {
            console.log("Deployment triggered successfully")
        } else {
            console.error("Failed to trigger deployment")
        }
    }
}