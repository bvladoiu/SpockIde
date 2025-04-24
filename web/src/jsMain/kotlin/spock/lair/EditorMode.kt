package spock.lair

import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.asList

fun enableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.className += " is-editing"

    val elements = document.querySelectorAll("*").asList()
    elements.forEach { node ->
        if (node is Element && node.textContent?.trim()?.isNotEmpty() == true) {
            node.setAttribute("contenteditable", "true")
            node.style {
                editableElement()
            }
            addControlIcons(node)
        }
    }
    addComponentControls()
    console.log("Edit mode enabled")
}

fun disableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.className = pageContent.className.replace("is-editing", "").trim()

    val elements = document.querySelectorAll("[contenteditable='true']").asList()
    elements.forEach { node ->
        if (node is Element) {
            node.removeAttribute("contenteditable")
            node.removeAttribute("style")
            val controlIcons = node.querySelector("div[data-control-icons='true']")
            if (controlIcons != null) {
                node.removeChild(controlIcons)
            }
        }
    }

    val buttons = document.querySelectorAll(".add-button, .remove-button").asList()
    for (button in buttons) {
        (button as Element).remove()
    }
    console.log("Edit mode disabled")
}