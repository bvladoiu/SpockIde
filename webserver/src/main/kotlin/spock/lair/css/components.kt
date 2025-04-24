package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for component styles.
 * This function returns a CSSBuilder with all the component styles.
 */
fun componentStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Hero section styles
        rule(".hero") {
            position = Position.relative
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
            padding(vertical = 80.px, horizontal = 20.px)
            textAlign = TextAlign.center
            backgroundColor = Color("#f0f0f0")
            borderRadius = LinearDimension("var(--border-radius-md)")
            overflow = Overflow.hidden
        }

        rule(".hero-content") {
            maxWidth = 800.px
            zIndex = 1
        }

        rule(".hero-title") {
            fontSize = LinearDimension("var(--font-size-h1)")
            marginBottom = 20.px
            color = Color("var(--color-text)")
        }

        rule(".hero-subtitle") {
            fontSize = LinearDimension("var(--font-size-h3)")
            marginBottom = 30.px
            color = Color("var(--color-text-light)")
        }

        rule(".hero-image") {
            position = Position.absolute
            top = 0.px
            left = 0.px
            width = 100.pct
            height = 100.pct
            objectFit = ObjectFit.cover
            opacity = 0.3
            zIndex = 0
        }

        // Competences section styles
        rule(".competences-grid") {
            display = Display.grid
            gridTemplateColumns = GridTemplateColumns("repeat(auto-fit, minmax(300px, 1fr))")
            put("gap", "30px")
        }

        rule(".competence-item") {
            padding(30.px)
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-md)")
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            transition("all", 0.3.s)

            hover {
                transform { translateY((-5).px) }
                put("box-shadow", "0 10px 20px rgba(0, 0, 0, 0.15)")
            }
        }

        rule(".competence-icon") {
            fontSize = 48.px
            marginBottom = 20.px
            color = Color("var(--color-primary)")
        }

        rule(".competence-title") {
            fontSize = LinearDimension("var(--font-size-h3)")
            marginBottom = 15.px
        }

        // Latest news section styles
        rule(".latest-grid") {
            display = Display.grid
            gridTemplateColumns = GridTemplateColumns("repeat(auto-fit, minmax(300px, 1fr))")
            put("gap", "30px")
        }

        // Posts section styles
        rule(".featured-post") {
            display = Display.grid
            gridTemplateColumns = GridTemplateColumns("1fr 1fr")
            put("gap", "30px")
            marginBottom = 50.px
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-md)")
            overflow = Overflow.hidden
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
        }

        rule(".featured-post-image") {
            width = 100.pct
            height = 100.pct
            objectFit = ObjectFit.cover
        }

        rule(".featured-post-content") {
            padding(30.px)
        }

        rule(".featured-post-title") {
            fontSize = 28.px
            marginBottom = 10.px
        }

        rule(".featured-post-date") {
            fontSize = 14.px
            color = Color("var(--color-text-light)")
            marginBottom = 15.px
        }

        rule(".featured-post-summary") {
            marginBottom = 20.px
        }

        rule(".posts-grid") {
            display = Display.grid
            gridTemplateColumns = GridTemplateColumns("repeat(auto-fit, minmax(300px, 1fr))")
            put("gap", "30px")
        }

        rule(".post-card") {
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-md)")
            overflow = Overflow.hidden
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            transition("all", 0.3.s)

            hover {
                transform { translateY((-5).px) }
                put("box-shadow", "0 10px 20px rgba(0, 0, 0, 0.15)")
            }
        }

        rule(".post-image") {
            width = 100.pct
            height = 200.px
            objectFit = ObjectFit.cover
        }

        rule(".post-content") {
            padding(20.px)
        }

        rule(".post-title") {
            fontSize = 20.px
            marginBottom = 10.px
        }

        rule(".post-date") {
            fontSize = 14.px
            color = Color("var(--color-text-light)")
            marginBottom = 10.px
        }

        rule(".post-summary") {
            marginBottom = 15.px
        }

        // Team section styles
        rule(".team-grid") {
            display = Display.grid
            gridTemplateColumns = GridTemplateColumns("repeat(auto-fit, minmax(300px, 1fr))")
            put("gap", "30px")
        }

        rule(".person-card") {
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-md)")
            overflow = Overflow.hidden
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            transition("all", 0.3.s)

            hover {
                transform { translateY((-5).px) }
                put("box-shadow", "0 10px 20px rgba(0, 0, 0, 0.15)")
            }
        }

        rule(".person-image") {
            width = 100.pct
            height = 300.px
            objectFit = ObjectFit.cover
        }

        rule(".person-content") {
            padding(20.px)
        }

        rule(".person-name") {
            fontSize = 24.px
            marginBottom = 5.px
        }

        rule(".person-position") {
            fontSize = 16.px
            color = Color("var(--color-text-light)")
            marginBottom = 15.px
            fontStyle = FontStyle.italic
        }

        rule(".person-bio") {
            marginBottom = 15.px
        }

        rule(".person-social") {
            display = Display.flex
            put("gap", "10px")
        }

        rule(".social-link") {
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.center
            width = 40.px
            height = 40.px
            backgroundColor = Color("#f0f0f0")
            borderRadius = 50.pct
            transition("all", 0.3.s)

            hover {
                backgroundColor = Color("var(--color-primary)")
                color = Color.white
            }
        }

        // Responsive styles
        media("(max-width: 768px)") {
            rule(".featured-post") {
                gridTemplateColumns = GridTemplateColumns("1fr")
            }

            rule(".hero-title") {
                fontSize = 36.px
            }

            rule(".hero-subtitle") {
                fontSize = 20.px
            }
        }
    }
}