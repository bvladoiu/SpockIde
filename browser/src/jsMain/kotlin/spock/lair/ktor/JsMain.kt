package spock.lair.ktor

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.asList
import org.w3c.dom.css.CSSStyleSheet
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.dom.url.URL

fun main() {
    document.addEventListener("keydown", { event ->
        val e = event as KeyboardEvent
        if (e.ctrlKey && e.shiftKey) {
            when (e.key.lowercase()) {
                "e" -> {
                    e.preventDefault()
                    makeTextElementsEditable()
                }
                "p" -> {
                    e.preventDefault()
                    disableEditableMode()
                }
                "s" -> {
                    e.preventDefault()
                    serializeAndDownload()
                }
            }
        }
    })

    document.addEventListener("click", { event ->
        val e = event as MouseEvent
        if (e.ctrlKey) {
            e.preventDefault()
            val target = e.target
            if (target is Element) {
                logStyles(target, "Initial (on Ctrl+Click)")
                var onAnimationOrTransitionEnd: ((Event) -> Unit)? = null
                onAnimationOrTransitionEnd = { event: Event ->
                    if (event.target == target) {
                        logStyles(target, "Final (after hover animation end)")
                        target.removeEventListener("transitionend", onAnimationOrTransitionEnd!!)
                        target.removeEventListener("animationend", onAnimationOrTransitionEnd)
                    }
                }
                var onMouseLeave: ((Event) -> Unit)? = null
                onMouseLeave = { event: Event ->
                    if (event.target == target) {
                        println("Mouse left element <${target.tagName.lowercase()} id='${target.id}'>. Waiting for animation end.")
                        target.addEventListener("transitionend", onAnimationOrTransitionEnd!!)
                        target.addEventListener("animationend", onAnimationOrTransitionEnd)
                        target.removeEventListener("mouseleave", onMouseLeave!!)
                    }
                }
                target.addEventListener("mouseleave", onMouseLeave)
            }
        }
    })
}

fun makeTextElementsEditable() {
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

    console.log("[Editable Mode] Applied contenteditable=true to all elements with visible text")
}

/**
 * Adds + and - control icons to the top right corner of the given element.
 * The - icon deletes the node, and the + icon duplicates it.
 */
private fun addControlIcons(element: Element) {
    // Check if the element already has control icons
    val existingContainer = element.querySelector("div[data-control-icons='true']")
    if (existingContainer != null) {
        // Remove existing control icons to avoid duplicates
        element.removeChild(existingContainer)
    }

    // Create container for icons using DSL
    val iconContainer = element("div") {
        attribute("data-control-icons", "true")
        attribute("contenteditable", "false") // Make sure the icons are not editable
        style {
            iconContainer()
        }
    }

    // Create delete (-) icon using DSL
    val deleteIcon = element("div") {
        text("-")
        attribute("contenteditable", "false")
        style {
            deleteIcon()
        }
        onClick { event ->
            event.stopPropagation()
            element.parentNode?.removeChild(element)
        }
    }

    // Create duplicate (+) icon using DSL
    val duplicateIcon = element("div") {
        text("+")
        attribute("contenteditable", "false")
        style {
            duplicateIcon()
        }
        onClick { event ->
            event.stopPropagation()
            val clone = element.cloneNode(true) as Element
            element.parentNode?.insertBefore(clone, element.nextSibling)

            // When we clone an element, we need to reattach event listeners
            // since they don't get cloned with the DOM structure
            addControlIcons(clone)
        }
    }

    // Add icons to container
    iconContainer.appendChild(deleteIcon)
    iconContainer.appendChild(duplicateIcon)

    // Add container to element
    element.appendChild(iconContainer)
}

fun disableEditableMode() {
    val elements = document.querySelectorAll("[contenteditable='true']").asList()

    elements.forEach { node ->
        if (node is Element) {
            // Remove contenteditable attribute
            node.removeAttribute("contenteditable")

            // Remove the editable style (red dashed outline)
            node.removeAttribute("style")

            // Remove control icons if they exist
            val controlIcons = node.querySelector("div[data-control-icons='true']")
            if (controlIcons != null) {
                node.removeChild(controlIcons)
            }
        }
    }

    console.log("[Preview Mode] Disabled editable mode for all elements")
}

fun serializeAndDownload() {
    // First ensure we're in preview mode
    disableEditableMode()

    // Get the HTML content
    val htmlContent = document.documentElement?.outerHTML ?: ""

    // Extract all CSS
    val cssContent = extractCSS()

    // Download HTML and CSS files
    downloadFile(htmlContent, "page.html", "text/html")
    downloadFile(cssContent, "styles.css", "text/css")

    console.log("[Serialize] Downloaded HTML and CSS files")
}

fun extractCSS(): String {
    val cssContent = StringBuilder()

    // Extract CSS from style elements
    val styleElements = document.querySelectorAll("style").asList()
    styleElements.forEach { element ->
        if (element is HTMLStyleElement) {
            cssContent.append(element.textContent)
            cssContent.append("\n\n")
        }
    }

    // Extract inline styles
    val elementsWithStyle = document.querySelectorAll("[style]").asList()
    elementsWithStyle.forEach { element ->
        if (element is Element) {
            val style = element.getAttribute("style")
            if (!style.isNullOrEmpty()) {
                val selector = generateSelector(element)
                cssContent.append("$selector {\n")
                cssContent.append("    $style\n")
                cssContent.append("}\n\n")
            }
        }
    }

    return cssContent.toString()
}

fun generateSelector(element: Element): String {
    // Create a simple selector based on tag name, id, and class
    val tagName = element.tagName.lowercase()
    val id = element.id
    val className = element.className

    return buildString {
        append(tagName)
        if (id.isNotEmpty()) {
            append("#$id")
        }
        if (className.isNotEmpty()) {
            // Use only the first class to keep it simple
            val firstClass = className.split(" ").firstOrNull()
            if (!firstClass.isNullOrEmpty()) {
                append(".$firstClass")
            }
        }
    }
}

fun downloadFile(content: String, filename: String, mimeType: String) {
    // Create a Blob with the content
    val blob = Blob(arrayOf(content), BlobPropertyBag(type = mimeType))
    val url = URL.createObjectURL(blob)

    // Create a download link
    val link = document.createElement("a") as HTMLAnchorElement
    link.href = url
    link.download = filename

    // Trigger download
    document.body?.appendChild(link)
    link.click()
    document.body?.removeChild(link)

    // Clean up
    URL.revokeObjectURL(url)
}

fun logStyles(element: Element, state: String) {
    val computed = window.getComputedStyle(element)
    val appliedStyles = mutableListOf<String>()

    for (i in 0 until computed.length) {
        val prop = computed.item(i)
        val value = computed.getPropertyValue(prop).trim()
        if (value.isNotEmpty()) {
            appliedStyles += "$prop: $value;"
        }
    }

    val elementIdentifier = buildString {
        append("<${element.tagName.lowercase()}>")
        if (element.id.isNotEmpty()) {
            append("#${element.id}")
        }
        // Add classes if desired for more specific identification
        // keeping this but i am inspecting elements with uninteligible class strings, i.e. i am trying to make computed styles heuristics to compile my own classes by explicitly ignoring this
        // if (element.className.isNotEmpty()) {
        //     append(".${element.className.split(' ').joinToString(".")}")
        // }
    }
    console.log("[$state computed styles for $elementIdentifier]:\n" + appliedStyles.joinToString("\n"))
}
