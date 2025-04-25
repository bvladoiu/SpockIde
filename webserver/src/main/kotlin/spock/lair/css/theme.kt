package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for styling theme variables.
 * This function returns a CSSBuilder with all the theme variables.
 * This is meant to be inlined in the head of the HTML document.
 */
fun themeStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // CSS Variables for theme colors
        root {
            // Primary colors
            put("--color-primary", "#0066cc")
            put("--color-primary-dark", "#004499")
            put("--color-primary-light", "#e6f7ff")

            // Secondary colors
            put("--color-secondary", "#ff9900")
            put("--color-secondary-dark", "#cc7a00")
            put("--color-secondary-light", "#ffe6cc")

            // Neutral colors
            put("--color-text", "#333333")
            put("--color-text-light", "#666666")
            put("--color-background", "#f8f8f8")
            put("--color-background-light", "#ffffff")
            put("--color-border", "#eeeeee")

            // Feedback colors
            put("--color-success", "#28a745")
            put("--color-warning", "#ffc107")
            put("--color-error", "#dc3545")
            put("--color-info", "#17a2b8")

            // Spacing
            put("--spacing-xs", "5px")
            put("--spacing-sm", "10px")
            put("--spacing-md", "20px")
            put("--spacing-lg", "30px")
            put("--spacing-xl", "60px")

            // Border radius
            put("--border-radius-sm", "4px")
            put("--border-radius-md", "8px")
            put("--border-radius-lg", "16px")
            put("--border-radius-circle", "50%")

            // Shadows
            put("--shadow-sm", "0 2px 5px rgba(0, 0, 0, 0.1)")
            put("--shadow-md", "0 4px 10px rgba(0, 0, 0, 0.1)")
            put("--shadow-lg", "0 10px 20px rgba(0, 0, 0, 0.15)")

            // Transitions
            put("--transition-fast", "0.2s")
            put("--transition-normal", "0.3s")
            put("--transition-slow", "0.5s")

            // Layout
            put("--container-width", "1200px")
            put("--header-height", "80px")
            put("--footer-height", "200px")

            // Breakpoints
            put("--breakpoint-sm", "576px")
            put("--breakpoint-md", "768px")
            put("--breakpoint-lg", "992px")
            put("--breakpoint-xl", "1200px")
        }
    }
}
