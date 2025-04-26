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
        // Root color scheme with base variables
        root {
            // Core color scheme variables
            put("--color", "#333333")
            put("--color-bg", "#f8f8f8")
            put("--color-primary", "#0066cc")
            put("--color-secondary", "#ff9900")

            // Extended color palette based on core variables
            put("--color-light", "#666666")
            put("--color-bg-light", "#ffffff")
            put("--color-primary-dark", "#004499")
            put("--color-primary-light", "#e6f7ff")
            put("--color-secondary-dark", "#cc7a00")
            put("--color-secondary-light", "#ffe6cc")
            put("--color-border", "#eeeeee")

            // Status colors
            put("--color-success", "#28a745")
            put("--color-warning", "#ffc107")
            put("--color-error", "#dc3545")
            put("--color-info", "#17a2b8")

            // Typography
            put("--font-family", "Arial, sans-serif")
            put("--font-size-base", "16px")
            put("--font-size-small", "14px")
            put("--font-size-large", "18px")
            put("--font-size-h1", "48px")
            put("--font-size-h2", "32px")
            put("--font-size-h3", "24px")
            put("--font-size-h4", "20px")

            // Fluid spacing using clamp (scales from 8px at 600px viewport to 24px at 1600px viewport)
            put("--spacing", "clamp(8px, calc(8px + (24 - 8) * ((100vw - 600px) / (1600 - 600))), 24px)")

            // Derived spacing values based on the fluid spacing
            put("--spacing-xs", "calc(var(--spacing) * 0.5)")
            put("--spacing-sm", "calc(var(--spacing) * 0.75)")
            put("--spacing-md", "var(--spacing)")
            put("--spacing-lg", "calc(var(--spacing) * 1.5)")
            put("--spacing-xl", "calc(var(--spacing) * 2.5)")

            // Border radius
            put("--border-radius-sm", "4px")
            put("--border-radius-md", "8px")
            put("--border-radius-lg", "16px")
            put("--border-radius-circle", "50%")

            // Fluid shadows (scales with viewport size)
            put("--shadow-intensity", "clamp(0.1, calc(0.1 + (0.2 - 0.1) * ((100vw - 600px) / (1600 - 600))), 0.2)")
            put("--shadow-sm", "0 2px 5px rgba(0, 0, 0, var(--shadow-intensity))")
            put("--shadow-md", "0 4px 10px rgba(0, 0, 0, var(--shadow-intensity))")
            put("--shadow-lg", "0 10px 20px rgba(0, 0, 0, calc(var(--shadow-intensity) * 1.5))")

            // Transition
            put("--transition", "0.4s")

            // Layout
            put("--container-width", "clamp(320px, 90vw, 1200px)")
            put("--header-height", "clamp(60px, 8vh, 80px)")
            put("--footer-height", "clamp(150px, 15vh, 200px)")

            // Breakpoints
            put("--breakpoint-sm", "576px")
            put("--breakpoint-md", "768px")
            put("--breakpoint-lg", "992px")
            put("--breakpoint-xl", "1200px")
        }

        // Dark theme (activated by adding .dark class to body)
        rule("body.dark") {
            put("--color", "#ffffff")
            put("--color-bg", "#222222")
            put("--color-light", "#cccccc")
            put("--color-bg-light", "#333333")
            put("--color-border", "#444444")

            // Adjust shadow intensity for dark mode
            put("--shadow-intensity", "clamp(0.3, calc(0.3 + (0.5 - 0.3) * ((100vw - 600px) / (1600 - 600))), 0.5)")
        }
    }
}
