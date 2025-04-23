package spock.lair.ktor

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent

fun main() {
    document.addEventListener("keydown", { event ->
        val e = event as KeyboardEvent
        if (e.ctrlKey && e.shiftKey && e.key.lowercase() == "e") {
            e.preventDefault()
            makeTextElementsEditable()
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

            // Override any restrictive styles that might prevent editing
            node.setAttribute("style", """
                outline: 1px dashed red;
                user-select: text !important;
                pointer-events: auto !important;
                min-height: 1em;
                cursor: text;
            """.trimIndent())
        }
    }

    console.log("[Editable Mode] Applied contenteditable=true to all elements with visible text")
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
