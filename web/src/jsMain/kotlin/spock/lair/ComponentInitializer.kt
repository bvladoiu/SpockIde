// file :web:jsMain:ComponentInitializer.kt
package spock.lair

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

/**
 * Type alias for component initializer functions.
 * These functions are called for each component of a specific type.
 */
typealias ComponentInitializer = (HTMLElement) -> Unit

/**
 * Registry of component initializers.
 * Maps component types to their initializer functions.
 */
private val componentInitializers = mutableMapOf<String, ComponentInitializer>()

/**
 * Registers a component initializer for a specific component type.
 * 
 * @param componentType The value of the data-component attribute
 * @param initializer The function to call for each component of this type
 */
fun registerComponent(componentType: String, initializer: ComponentInitializer) {
    componentInitializers[componentType] = initializer
}

/**
 * Initializes all components on the page based on their data-component attributes.
 * This function finds all elements with data-component attributes and calls the appropriate initializer.
 */
fun initComponents() {
    val components = document.querySelectorAll("[data-component]")

    for (i in 0 until components.length) {
        val element = components[i] as? HTMLElement ?: continue
        val componentType = element.getAttribute("data-component") ?: continue

        // Call the initializer for this component type if registered
        componentInitializers[componentType]?.invoke(element)
    }
}

