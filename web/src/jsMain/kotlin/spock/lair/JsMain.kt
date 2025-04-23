// file :web:jsMain:JsMain.kt
package spock.lair

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import org.w3c.dom.asList
import org.w3c.dom.events.KeyboardEvent
import org.w3c.fetch.RequestInit
import kotlin.js.Date

fun main() {
    window.onload = {
        setupEditor()
    }
}

fun setupEditor() {
    // Set up keyboard shortcuts
    document.addEventListener("keydown", { event ->
        val e = event as KeyboardEvent

        // Use Ctrl+number combinations instead of function keys
        // Ctrl key is represented by e.ctrlKey
        if (e.ctrlKey) {
            when (e.key) {
                "2" -> { 
                    e.preventDefault()
                    toggleTheme() 
                }
                "3" -> { 
                    e.preventDefault()
                    enableEditMode() 
                }
                "4" -> { 
                    e.preventDefault()
                    disableEditMode() 
                }
                "5" -> { 
                    e.preventDefault()
                    savePage() 
                }
                "8" -> { 
                    e.preventDefault()
                    loadPage() 
                }
                "9" -> { 
                    e.preventDefault()
                    deployPage() 
                }
            }
        }
    })

    console.log("Editor initialized")
}

fun enableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.className += " is-editing"

    // Make editable elements actually editable
    val editableElements = pageContent.querySelectorAll("[data-editable='true']").asList()
    for (element in editableElements) {
        (element as Element).setAttribute("contenteditable", "true")
    }

    // Add component controls
    addComponentControls()

    console.log("Edit mode enabled")
}

fun disableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.className = pageContent.className.replace("is-editing", "").trim()

    // Make elements non-editable
    val editableElements = pageContent.querySelectorAll("[data-editable='true']").asList()
    for (element in editableElements) {
        (element as Element).removeAttribute("contenteditable")
    }

    // Remove all control buttons
    val buttons = document.querySelectorAll(".add-button, .remove-button").asList()
    for (button in buttons) {
        (button as Element).remove()
    }

    console.log("Edit mode disabled")
}

fun addComponentControls() {
    val pageContent = document.getElementById("page-content") ?: return

    // Add remove buttons to components
    val components = pageContent.querySelectorAll("[data-component]").asList()
    for (comp in components) {
        val component = comp as Element
        val removeBtn = document.createElement("button") as HTMLButtonElement
        removeBtn.className = "remove-button"
        removeBtn.textContent = "-"
        removeBtn.onclick = { component.remove() }
        component.appendChild(removeBtn)

        // Add "add" button after this component
        val addBtn = document.createElement("button") as HTMLButtonElement
        addBtn.className = "add-button"
        addBtn.textContent = "+"
        addBtn.onclick = { 
            addComponent("contentSection", component)
        }

        // Insert after component
        val parent = component.parentElement
        if (parent != null) {
            if (component.nextElementSibling != null) {
                parent.insertBefore(addBtn, component.nextElementSibling)
            } else {
                parent.appendChild(addBtn)
            }
        }
    }
}

fun addComponent(type: String, targetElement: Element) {
    val newId = "component-${Date.now()}"
    val newElement = document.createElement("div") as HTMLDivElement

    newElement.setAttribute("data-component", type)
    newElement.setAttribute("data-id", newId)

    when (type) {
        "heroSection" -> {
            newElement.innerHTML = """
                <h1 data-editable="true">New Hero Title</h1>
            """.trimIndent()
        }
        "contentSection" -> {
            newElement.innerHTML = """
                <h2 data-editable="true">New Section</h2>
                <p data-editable="true">New content goes here.</p>
            """.trimIndent()
        }
    }

    // Add remove button
    val removeBtn = document.createElement("button") as HTMLButtonElement
    removeBtn.className = "remove-button"
    removeBtn.textContent = "-"
    removeBtn.onclick = { newElement.remove() }
    newElement.appendChild(removeBtn)

    // Insert after target
    val parent = targetElement.parentElement
    if (parent != null) {
        if (targetElement.nextElementSibling != null) {
            parent.insertBefore(newElement, targetElement.nextElementSibling)
        } else {
            parent.appendChild(newElement)
        }
    }

    // Add an "add" button after the new component
    val addBtn = document.createElement("button") as HTMLButtonElement
    addBtn.className = "add-button"
    addBtn.textContent = "+"
    addBtn.onclick = { 
        addComponent("contentSection", newElement)
    }

    // Insert after new element
    if (parent != null) {
        if (newElement.nextElementSibling != null) {
            parent.insertBefore(addBtn, newElement.nextElementSibling)
        } else {
            parent.appendChild(addBtn)
        }
    }

    // Make the new elements editable
    val editableElements = newElement.querySelectorAll("[data-editable='true']").asList()
    for (element in editableElements) {
        (element as Element).setAttribute("contenteditable", "true")
    }
}

fun toggleTheme() {
    val body = document.body
    if (body != null) {
        if (body.getAttribute("data-theme") == "dark") {
            body.removeAttribute("data-theme")
        } else {
            body.setAttribute("data-theme", "dark")
        }
    }
    console.log("Theme toggled")
}

fun savePage() {
    val pageContent = document.getElementById("page-content") ?: return
    val content = pageContent.innerHTML

    // Remove editing artifacts before saving
    val tempDiv = document.createElement("div")
    tempDiv.innerHTML = content

    val buttons = tempDiv.querySelectorAll(".add-button, .remove-button").asList()
    for (button in buttons) {
        (button as Element).remove()
    }

    val cleanContent = tempDiv.innerHTML

    window.fetch("/api/save/home", RequestInit(
        method = "POST",
        body = cleanContent
    )).then { response ->
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

                    // Rebind controls if in edit mode
                    if (pageContent.className.contains("is-editing")) {
                        rebindEditingControls()
                    }
                }

                console.log("Page loaded successfully")
            }
        } else {
            console.error("Failed to load page")
        }
    }
}

fun rebindEditingControls() {
    // Re-add component controls if we're in edit mode
    addComponentControls()

    // Make editable elements actually editable again
    val pageContent = document.getElementById("page-content") ?: return
    val editableElements = pageContent.querySelectorAll("[data-editable='true']").asList()
    for (element in editableElements) {
        (element as Element).setAttribute("contenteditable", "true")
    }
}

fun deployPage() {
    window.fetch("/api/deploy/home", RequestInit(
        method = "POST"
    )).then { response ->
        if (response.ok) {
            console.log("Deployment triggered successfully")
        } else {
            console.error("Failed to trigger deployment")
        }
    }
}
