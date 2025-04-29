package spock.lair.css

import kotlinx.css.*
import kotlinx.css.properties.*

/**
 * CSS DSL for styling articles.
 * This function returns a CSSBuilder with all the styles for articles.
 */
fun articleStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Article container
        rule(".article") {
            maxWidth = 800.px
            margin(LinearDimension.auto)
            padding(LinearDimension("var(--spacing-md)"))
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-md)")
            put("box-shadow", "var(--shadow-md)")
        }

        // Article header
        rule(".article-header") {
            marginBottom = 30.px
        }

        rule(".article-title") {
            fontSize = LinearDimension("var(--font-size-h1)")
            marginBottom = 10.px
            color = Color("var(--color-text)")
        }

        rule(".article-meta") {
            display = Display.flex
            alignItems = Align.center
            marginBottom = 20.px
            color = Color("var(--color-text-light)")
            fontSize = LinearDimension("var(--font-size-small)")
        }

        rule(".article-date") {
            marginRight = 20.px
        }

        rule(".article-author") {
            fontWeight = FontWeight.bold
        }

        rule(".article-image") {
            width = 100.pct
            height = LinearDimension.auto
            maxHeight = 500.px
            objectFit = ObjectFit.cover
            borderRadius = 4.px
            marginBottom = 30.px
        }

        // Article content
        rule(".article-content") {
            lineHeight = LineHeight("1.8")

            children("h2") {
                fontSize = 28.px
                marginTop = 40.px
                marginBottom = 20.px
            }

            children("h3") {
                fontSize = 24.px
                marginTop = 30.px
                marginBottom = 15.px
            }

            children("p") {
                marginBottom = 20.px
            }

            children("blockquote") {
                borderLeft = "4px solid var(--color-blockquote)"
                paddingLeft = 20.px
                fontStyle = FontStyle.italic
                color = Color("var(--color-text-light)")
                margin(vertical = 20.px, horizontal = 0.px)
            }

            children("ul, ol") {
                marginBottom = 20.px
                paddingLeft = 20.px

                children("li") {
                    marginBottom = 10.px
                }
            }

            children("img") {
                maxWidth = 100.pct
                height = LinearDimension.auto
                borderRadius = 4.px
                marginBottom = 20.px
            }

            children("code") {
                fontFamily = "monospace"
                backgroundColor = Color("var(--color-code-bg)")
                padding(vertical = 2.px, horizontal = 5.px)
                borderRadius = LinearDimension("var(--border-radius-sm)")
                fontSize = LinearDimension("var(--font-size-small)")
            }

            children("pre") {
                backgroundColor = Color("var(--color-code-bg)")
                padding(15.px)
                borderRadius = LinearDimension("var(--border-radius-sm)")
                overflow = Overflow.auto
                marginBottom = 20.px

                children("code") {
                    padding(0.px)
                    backgroundColor = Color("transparent")
                }
            }
        }

        // Article tags
        rule(".article-tags") {
            display = Display.flex
            flexWrap = FlexWrap.wrap
            marginTop = 30.px
            marginBottom = 30.px
        }

        rule(".article-tag") {
            display = Display.inlineBlock
            padding(vertical = 5.px, horizontal = 10.px)
            backgroundColor = Color("var(--color-tag-bg)")
            borderRadius = LinearDimension("var(--border-radius-lg)")
            marginRight = 10.px
            marginBottom = 10.px
            fontSize = LinearDimension("var(--font-size-small)")
            color = Color("var(--color-text-light)")
            transition("all", 0.3.s)

            hover {
                backgroundColor = Color("var(--color-primary)")
                color = Color("var(--color-bg-light)")
            }
        }

        rule(".related-articles") {
            marginTop = 50.px
            borderTop = "1px solid var(--color-border-light)"
            paddingTop = 30.px
        }

        rule(".related-articles-title") {
            fontSize = 24.px
            marginBottom = 20.px
        }

        rule(".related-articles-grid") {
            display = Display.grid
            gridTemplateColumns = GridTemplateColumns("repeat(auto-fill, minmax(250px, 1fr))")
            gap = Gap("20px")
        }

        rule(".related-article") {
            backgroundColor = Color("var(--color-background-light)")
            borderRadius = LinearDimension("var(--border-radius-md)")
            overflow = Overflow.hidden
            put("box-shadow", "var(--shadow-md)")
            transition("all", 0.3.s)

            hover {
                transform {
                    translateY((-5).px)
                }
                put("box-shadow", "var(--shadow-lg)")
            }
        }

        rule(".related-article-image") {
            width = 100.pct
            height = 150.px
            objectFit = ObjectFit.cover
        }

        rule(".related-article-content") {
            padding(15.px)
        }

        rule(".related-article-title") {
            fontSize = 18.px
            marginBottom = 10.px
        }

        rule(".related-article-date") {
            fontSize = LinearDimension("var(--font-size-small)")
            color = Color("var(--color-text-light)")
        }
    }
}
