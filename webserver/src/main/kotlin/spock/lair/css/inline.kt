package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for critical inline styles.
 * This function returns a CSSBuilder with essential styles that should be inlined in every page.
 * These styles are duplicated from their original files to ensure they're loaded immediately.
 */
fun inlineStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Critical CSS Variables (from theme.kt)
        root {
            // Primary colors
            put("--color-primary", "#0066cc")
            put("--color-primary-dark", "#004499")
            
            // Text colors
            put("--color-text", "#333333")
            put("--color-background", "#f8f8f8")
            
            // Spacing
            put("--spacing-md", "20px")
            
            // Container width
            put("--container-width", "1200px")
        }
        
        // Critical Typography Variables (from typography.kt)
        root {
            put("--font-family", "Arial, sans-serif")
            put("--font-size-base", "16px")
            put("--font-size-h1", "48px")
            put("--font-size-h2", "32px")
        }
        
        // Critical Global Styles (from layout.kt)
        rule("*, *::before, *::after") {
            boxSizing = BoxSizing.borderBox
        }
        
        // Critical Typography Styles (from typography.kt)
        body {
            fontFamily = "var(--font-family)"
            fontSize = LinearDimension("var(--font-size-base)")
            lineHeight = LineHeight("1.6")
            color = Color("var(--color-text)")
        }
        
        // Critical Heading Styles (from typography.kt)
        rule("h1, h2") {
            margin(top = 0.px, bottom = 20.px)
            fontWeight = FontWeight.bold
            lineHeight = LineHeight("1.2")
        }
        
        // Critical Link Styles (from typography.kt)
        rule("a") {
            color = Color("var(--color-primary)")
            textDecoration = TextDecoration.none
        }
        
        // Critical Container Styles (from layout.kt)
        rule("#page-content") {
            maxWidth = LinearDimension("var(--container-width)")
            margin(LinearDimension.auto)
            padding(20.px)
        }
    }
}