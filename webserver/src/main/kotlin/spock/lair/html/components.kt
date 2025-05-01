package spock.lair.html

import kotlinx.html.*


fun FlowContent.element(
    tag: String? = "div",
    id: String? = null,
    classes: String? = null,
    content: String,
    attributes: Map<String, String> = emptyMap(),
    block: (FlowContent.() -> Unit)? = null
) {
    when (tag) {
        "div" -> div {
            +content
            this.attributes.putAll(attributes)
            id?.let { this.id = it }
            classes?.let { this.classes += it }
            block?.invoke(this)
        }

        "section" -> section {
            +content
            this.attributes.putAll(attributes)
            id?.let { this.id = it }
            classes?.let { this.classes += it }
            block?.invoke(this)
        }

        "article" -> article {
            +content
            this.attributes.putAll(attributes)
            id?.let { this.id = it }
            classes?.let { this.classes += it }
            block?.invoke(this)
        }
    }

}



fun FlowContent.langSelect() {
    div { +"Todo:langSelect" }
}

fun FlowContent.logoLink() {
    div { +"Todo:logoLink" }
}

fun FlowContent.cta() {
    div { +"Todo:cta" }
}

fun FlowContent.footer() {
    div { +"Todo:footer" }
}


/**
 * Creates a theme switcher component.
 * This component allows users to toggle between light and dark themes.
 */
fun FlowContent.themeSwitch() {
    div {
        classes = setOf("theme-switcher")
        attributes["data-component"] = "theme-switcher"

        button {
            classes = setOf("theme-toggle")
            attributes["aria-label"] = "Toggle dark mode"
            attributes["title"] = "Toggle dark mode"

            span {
                classes = setOf("theme-toggle-icon", "light-icon")
                +"☀️"
            }
            span {
                classes = setOf("theme-toggle-icon", "dark-icon")
                +"🌙"
            }
        }
    }
}
