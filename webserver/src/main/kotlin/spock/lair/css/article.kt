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
            padding(20.px)
            put("background-color", "#ffffff")
            borderRadius = 8.px
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
        }

        // Article header
        rule(".article-header") {
            marginBottom = 30.px
        }

        rule(".article-title") {
            fontSize = 36.px
            marginBottom = 10.px
            put("color", "#333333")
        }

        rule(".article-meta") {
            display = Display.flex
            alignItems = Align.center
            marginBottom = 20.px
            put("color", "#666666")
            fontSize = 14.px
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
                borderLeft = "4px solid #0066cc"
                paddingLeft = 20.px
                fontStyle = FontStyle.italic
                put("color", "#555555")
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
                put("background-color", "#f5f5f5")
                padding(vertical = 2.px, horizontal = 5.px)
                borderRadius = 3.px
                fontSize = 14.px
            }

            children("pre") {
                put("background-color", "#f5f5f5")
                padding(15.px)
                borderRadius = 4.px
                overflow = Overflow.auto
                marginBottom = 20.px

                children("code") {
                    padding(0.px)
                    put("background-color", "transparent")
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
            backgroundColor = Color("#f0f0f0")
            borderRadius = 20.px
            marginRight = 10.px
            marginBottom = 10.px
            fontSize = 14.px
            color = Color("#666")

            hover {
                backgroundColor = Color("#0066cc")
                color = Color.white
            }
        }

        rule(".related-articles") {
            marginTop = 50.px
            borderTop = "1px solid #eee"
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
            put("background-color", "#ffffff")
            borderRadius = 8.px
            overflow = Overflow.hidden
            put("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
            transition("all", 0.3.s)

            hover {
                transform {
                    translateY((-5).px)
                }
                put("box-shadow", "0 10px 20px rgba(0, 0, 0, 0.15)")
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
            fontSize = 14.px
            color = Color("#666")
        }
    }
}
