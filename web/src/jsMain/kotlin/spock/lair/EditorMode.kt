package spock.lair

import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.asList

/**
 * Functions for managing editor mode
 */

/**
 * Enables edit mode for the page content
 */
fun enableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.className += " is-editing"

    // Make all text elements editable (like in browser module)
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

    // Also maintain the original component controls for backward compatibility
    addComponentControls()

    console.log("Edit mode enabled")
}

/**
 * Disables edit mode for the page content
 */
fun disableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.className = pageContent.className.replace("is-editing", "").trim()

    // Make all elements non-editable (like in browser module)
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

    // Also remove the original component control buttons for backward compatibility
    val buttons = document.querySelectorAll(".add-button, .remove-button").asList()
    for (button in buttons) {
        (button as Element).remove()
    }

    console.log("Edit mode disabled")
}

fun rebindEditingControls() {
    val pageContent = document.getElementById("page-content") ?: return

    addComponentControls()

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
}