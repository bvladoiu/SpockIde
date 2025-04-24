package spock.lair

import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.asList
import kotlin.js.Date

fun addComponentControls() {
    val pageContent = document.getElementById("page-content") ?: return
    val components = pageContent.querySelectorAll("[data-component]").asList()
    for (comp in components) {
        val component = comp as Element
        val removeBtn = document.createElement("button") as HTMLButtonElement
        removeBtn.className = "remove-button"
        removeBtn.textContent = "-"
        removeBtn.onclick = { component.remove() }
        component.appendChild(removeBtn)

        val addBtn = document.createElement("button") as HTMLButtonElement
        addBtn.className = "add-button"
        addBtn.textContent = "+"
        addBtn.onclick = {
            addComponent("contentSection", component)
        }

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

    val removeBtn = document.createElement("button") as HTMLButtonElement
    removeBtn.className = "remove-button"
    removeBtn.textContent = "-"
    removeBtn.onclick = { newElement.remove() }
    newElement.appendChild(removeBtn)

    val parent = targetElement.parentElement
    if (parent != null) {
        if (targetElement.nextElementSibling != null) {
            parent.insertBefore(newElement, targetElement.nextElementSibling)
        } else {
            parent.appendChild(newElement)
        }
    }

    val addBtn = document.createElement("button") as HTMLButtonElement
    addBtn.className = "add-button"
    addBtn.textContent = "+"
    addBtn.onclick = {
        addComponent("contentSection", newElement)
    }

    if (parent != null) {
        if (newElement.nextElementSibling != null) {
            parent.insertBefore(addBtn, newElement.nextElementSibling)
        } else {
            parent.appendChild(addBtn)
        }
    }

    val editableElements = newElement.querySelectorAll("[data-editable='true']").asList()
    for (element in editableElements) {
        (element as Element).setAttribute("contenteditable", "true")
    }
}

fun addControlIcons(element: Element) {
    val existingContainer = element.querySelector("div[data-control-icons='true']")
    if (existingContainer != null) {
        element.removeChild(existingContainer)
    }
    val iconContainer = element("div") {
        attribute("data-control-icons", "true")
        attribute("contenteditable", "false")
        style {
            iconContainer()
        }
    }
    val deleteIcon = element("div") {
        text("-")
        attribute("contenteditable", "false")
        style {
            controlIcon()
        }
        onClick { event ->
            event.stopPropagation()
            element.parentNode?.removeChild(element)
        }
    }
    val duplicateIcon = element("div") {
        text("+")
        attribute("contenteditable", "false")
        style {
            controlIcon()
        }
        onClick { event ->
            event.stopPropagation()
            val clone = element.cloneNode(true) as Element
            element.parentNode?.insertBefore(clone, element.nextSibling)
            addControlIcons(clone)
        }
    }
    iconContainer.appendChild(deleteIcon)
    iconContainer.appendChild(duplicateIcon)
    element.appendChild(iconContainer)
}