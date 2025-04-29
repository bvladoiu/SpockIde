package spock.lair

import io.ktor.server.html.*
import kotlinx.html.*
import spock.lair.css.*
import java.io.File

/**
 * DSL for building pages using a scaffold approach.
 * This function takes a PageObject and a block of HTML content to build a page.
 * 
 * @param pageObject The page object containing page metadata
 * @param isDevelopment Whether the application is running in development mode
 * @param block The block of HTML content to build the page
 */
fun HTML.scaffold(pageObject: PageObject, isDevelopment: Boolean = false, block: ScaffoldContext.() -> Unit) {
    val scaffoldContext = ScaffoldContext(pageObject)

    head {
        title { +scaffoldContext.getPageTitle() }
        meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
        meta(charset = "UTF-8")

        if (isDevelopment) {
            // In development mode, load individual CSS files for easier debugging
            link(rel = "stylesheet", href = "/static/css/dev/theme.css")
            link(rel = "stylesheet", href = "/static/css/dev/typography.css")
            link(rel = "stylesheet", href = "/static/css/dev/layout.css")
            link(rel = "stylesheet", href = "/static/css/dev/section.css")
            link(rel = "stylesheet", href = "/static/css/dev/button.css")
            link(rel = "stylesheet", href = "/static/css/dev/component.css")
            link(rel = "stylesheet", href = "/static/css/dev/article.css")
            link(rel = "stylesheet", href = "/static/css/dev/lang-select.css")
            link(rel = "stylesheet", href = "/static/css/dev/editor.css")
            link(rel = "stylesheet", href = "/static/css/dev/dark-mode.css")
        } else {
            // In production mode, include critical inline styles and load the aggregated CSS
            style { unsafe { +inlineStyles().toString() } }
            link(rel = "stylesheet", href = "/static/css/app.css")
        }
    }

    body {
        div {
            id = "page-content"

            // Create a content DIV element and pass it to the ScaffoldContext
            scaffoldContext.contentDiv = this
            scaffoldContext.apply(block)
        }

        script(src = "/static/common/web.js") {
            attributes["defer"] = "true"
        }
        script(src = "/static/main.js") {
            attributes["defer"] = "true"
        }
    }
}

/**
 * Context class for the scaffold DSL.
 * This class provides access to the page object and methods for building page sections.
 */
class ScaffoldContext(val pageObject: PageObject) {
    // Content DIV element to use as a FlowContent receiver
    lateinit var contentDiv: DIV

    private val pageContent: String by lazy {
        val file = File(pageObject.getMetaPath())
        if (file.exists()) file.readText() else "{}"
    }

    /**
     * Get the page title from the page data.
     */
    fun getPageTitle(): String {
        val titleRegex = """"title"\s*:\s*"([^"]+)"""".toRegex()
        val matchResult = titleRegex.find(pageContent)
        return matchResult?.groupValues?.get(1) ?: "Page"
    }

    /**
     * Convenience method to create a hero section.
     * This method uses the contentDiv as a FlowContent receiver.
     */
    fun hero() {
        with(contentDiv as FlowContent) {
            hero()
        }
    }

    /**
     * Convenience method to create a competences section.
     * This method uses the contentDiv as a FlowContent receiver.
     */
    fun competences() {
        with(contentDiv as FlowContent) {
            competences()
        }
    }

    /**
     * Convenience method to create a latest news section.
     * This method uses the contentDiv as a FlowContent receiver.
     */
    fun latest() {
        with(contentDiv as FlowContent) {
            latest()
        }
    }

    /**
     * Convenience method to create a posts grid section.
     * This method uses the contentDiv as a FlowContent receiver.
     */
    fun posts() {
        with(contentDiv as FlowContent) {
            posts()
        }
    }

    /**
     * Convenience method to create a team section.
     * This method uses the contentDiv as a FlowContent receiver.
     */
    fun team() {
        with(contentDiv as FlowContent) {
            team()
        }
    }

    /**
     * Convenience method to create a custom section.
     * This method uses the contentDiv as a FlowContent receiver.
     */
    fun section(name: String, block: FlowContent.() -> Unit) {
        with(contentDiv as FlowContent) {
            section(name, block)
        }
    }

    /**
     * Create a section with the given name and content.
     */
    fun FlowContent.section(name: String, block: FlowContent.() -> Unit) {
        section {
            id = name
            classes = setOf("section", name)
            block()
        }
    }

    /**
     * Create a hero section with content from the page data.
     */
    fun FlowContent.hero() {
        // Extract hero section data
        val heroRegex = """"hero"\s*:\s*\{([^}]+)\}""".toRegex()
        val sectionsHeroRegex = """"sections"\s*:\s*\{[^}]*"hero"\s*:\s*\{([^}]+)\}""".toRegex()

        val heroContent = heroRegex.find(pageContent)?.groupValues?.get(1)
            ?: sectionsHeroRegex.find(pageContent)?.groupValues?.get(1)

        if (heroContent != null) {
            section("hero") {
                div {
                    classes = setOf("hero-content")
                    h1 {
                        classes = setOf("hero-title")
                        +extractValue(heroContent, "title")
                    }

                    val subtitle = extractValue(heroContent, "subtitle")
                    if (subtitle.isNotEmpty()) {
                        p {
                            classes = setOf("hero-subtitle")
                            +subtitle
                        }
                    }

                    val ctaRegex = """"cta"\s*:\s*\{([^}]+)\}""".toRegex()
                    val ctaContent = ctaRegex.find(heroContent)?.groupValues?.get(1)

                    if (ctaContent != null) {
                        a(href = extractValue(ctaContent, "url")) {
                            classes = setOf("hero-cta", "button")
                            +extractValue(ctaContent, "text")
                        }
                    }
                }

                val image = extractValue(heroContent, "image")
                if (image.isNotEmpty()) {
                    img(src = image) {
                        classes = setOf("hero-image")
                        alt = "Hero Image"
                    }
                }
            }
        }
    }

    /**
     * Create a competences section with content from the page data.
     */
    fun FlowContent.competences() {
        // Extract competences section data
        val competencesRegex = """"competences"\s*:\s*\{([^}]+)\}""".toRegex()
        val sectionsCompetencesRegex = """"sections"\s*:\s*\{[^}]*"competences"\s*:\s*\{([^}]+)\}""".toRegex()

        val competencesContent = competencesRegex.find(pageContent)?.groupValues?.get(1)
            ?: sectionsCompetencesRegex.find(pageContent)?.groupValues?.get(1)

        if (competencesContent != null) {
            section("competences") {
                h2 {
                    classes = setOf("section-title")
                    +extractValue(competencesContent, "title")
                }

                div {
                    classes = setOf("competences-grid")

                    val itemsRegex = """"items"\s*:\s*\[\s*(.*?)\s*\]""".toRegex(RegexOption.DOT_MATCHES_ALL)
                    val itemsContent = itemsRegex.find(competencesContent)?.groupValues?.get(1)

                    if (itemsContent != null) {
                        val itemRegex = """\{\s*(.*?)\s*\}""".toRegex(RegexOption.DOT_MATCHES_ALL)
                        val items = itemRegex.findAll(itemsContent)

                        items.forEach { itemMatch ->
                            val itemContent = itemMatch.groupValues[1]

                            div {
                                classes = setOf("competence-item")

                                div {
                                    classes = setOf("competence-icon")
                                    i {
                                        classes = setOf("icon", "icon-${extractValue(itemContent, "icon")}")
                                    }
                                }

                                h3 {
                                    classes = setOf("competence-title")
                                    +extractValue(itemContent, "title")
                                }

                                p {
                                    classes = setOf("competence-description")
                                    +extractValue(itemContent, "description")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create a latest news section with content from the page data.
     */
    fun FlowContent.latest() {
        // Extract latest section data
        val latestRegex = """"latest"\s*:\s*\{([^}]+)\}""".toRegex()
        val sectionsLatestRegex = """"sections"\s*:\s*\{[^}]*"latest"\s*:\s*\{([^}]+)\}""".toRegex()

        val latestContent = latestRegex.find(pageContent)?.groupValues?.get(1)
            ?: sectionsLatestRegex.find(pageContent)?.groupValues?.get(1)

        if (latestContent != null) {
            section("latest") {
                h2 {
                    classes = setOf("section-title")
                    +extractValue(latestContent, "title")
                }

                div {
                    classes = setOf("latest-grid")

                    val postsRegex = """"posts"\s*:\s*\[\s*(.*?)\s*\]""".toRegex(RegexOption.DOT_MATCHES_ALL)
                    val postsContent = postsRegex.find(latestContent)?.groupValues?.get(1)

                    if (postsContent != null) {
                        val postRegex = """\{\s*(.*?)\s*\}""".toRegex(RegexOption.DOT_MATCHES_ALL)
                        val posts = postRegex.findAll(postsContent)

                        posts.forEach { postMatch ->
                            val postContent = postMatch.groupValues[1]

                            div {
                                classes = setOf("post-card")

                                h3 {
                                    classes = setOf("post-title")
                                    +extractValue(postContent, "title")
                                }

                                val date = extractValue(postContent, "date")
                                if (date.isNotEmpty()) {
                                    p {
                                        classes = setOf("post-date")
                                        +date
                                    }
                                }

                                p {
                                    classes = setOf("post-summary")
                                    +extractValue(postContent, "summary")
                                }

                                val url = extractValue(postContent, "url")
                                if (url.isNotEmpty()) {
                                    a(href = url) {
                                        classes = setOf("post-link")
                                        +"Read More"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create a posts grid section with content from the page data.
     */
    fun FlowContent.posts() {
        // Extract posts data
        val postsRegex = """"posts"\s*:\s*\[\s*(.*?)\s*\]""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val postsContent = postsRegex.find(pageContent)?.groupValues?.get(1)

        if (postsContent != null) {
            section("posts") {
                h2 {
                    classes = setOf("section-title")
                    +getPageTitle()
                }

                p {
                    classes = setOf("section-description")
                    +extractValue(pageContent, "description")
                }

                // Find featured post
                val postRegex = """\{\s*(.*?)\s*\}""".toRegex(RegexOption.DOT_MATCHES_ALL)
                val posts = postRegex.findAll(postsContent).toList()

                var featuredPost: MatchResult? = null
                for (post in posts) {
                    val postContent = post.groupValues[1]
                    if (postContent.contains(""""featured"\s*:\s*true""")) {
                        featuredPost = post
                        break
                    }
                }

                // If no featured post is found, use the first one
                if (featuredPost == null && posts.isNotEmpty()) {
                    featuredPost = posts.first()
                }

                if (featuredPost != null) {
                    val featuredPostContent = featuredPost.groupValues[1]

                    div {
                        classes = setOf("featured-post")

                        val image = extractValue(featuredPostContent, "image")
                        if (image.isNotEmpty()) {
                            img(src = image) {
                                classes = setOf("featured-post-image")
                                alt = extractValue(featuredPostContent, "title")
                            }
                        }

                        div {
                            classes = setOf("featured-post-content")

                            h3 {
                                classes = setOf("featured-post-title")
                                +extractValue(featuredPostContent, "title")
                            }

                            val date = extractValue(featuredPostContent, "date")
                            if (date.isNotEmpty()) {
                                p {
                                    classes = setOf("featured-post-date")
                                    +date
                                }
                            }

                            p {
                                classes = setOf("featured-post-summary")
                                +extractValue(featuredPostContent, "summary")
                            }

                            val id = extractValue(featuredPostContent, "id")
                            a(href = "/${pageObject.locale}/article/$id") {
                                classes = setOf("featured-post-link", "button")
                                +"Read More"
                            }
                        }
                    }
                }

                // Regular posts grid (excluding the featured post)
                div {
                    classes = setOf("posts-grid")

                    for (post in posts) {
                        val postContent = post.groupValues[1]

                        // Skip the featured post
                        if (post == featuredPost) continue

                        div {
                            classes = setOf("post-card")

                            val image = extractValue(postContent, "image")
                            if (image.isNotEmpty()) {
                                img(src = image) {
                                    classes = setOf("post-image")
                                    alt = extractValue(postContent, "title")
                                }
                            }

                            div {
                                classes = setOf("post-content")

                                h3 {
                                    classes = setOf("post-title")
                                    +extractValue(postContent, "title")
                                }

                                val date = extractValue(postContent, "date")
                                if (date.isNotEmpty()) {
                                    p {
                                        classes = setOf("post-date")
                                        +date
                                    }
                                }

                                p {
                                    classes = setOf("post-summary")
                                    +extractValue(postContent, "summary")
                                }

                                val id = extractValue(postContent, "id")
                                a(href = "/${pageObject.locale}/article/$id") {
                                    classes = setOf("post-link")
                                    +"Read More"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create a team section with content from the page data.
     */
    fun FlowContent.team() {
        // Extract team data
        val teamRegex = """"team"\s*:\s*\[\s*(.*?)\s*\]""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val teamContent = teamRegex.find(pageContent)?.groupValues?.get(1)

        if (teamContent != null) {
            section("team") {
                h2 {
                    classes = setOf("section-title")
                    +getPageTitle()
                }

                p {
                    classes = setOf("section-description")
                    +extractValue(pageContent, "description")
                }

                div {
                    classes = setOf("team-grid")

                    val personRegex = """\{\s*(.*?)\s*\}""".toRegex(RegexOption.DOT_MATCHES_ALL)
                    val persons = personRegex.findAll(teamContent)

                    persons.forEach { personMatch ->
                        val personContent = personMatch.groupValues[1]

                        div {
                            classes = setOf("person-card")

                            val image = extractValue(personContent, "image")
                            if (image.isNotEmpty()) {
                                img(src = image) {
                                    classes = setOf("person-image")
                                    alt = extractValue(personContent, "name")
                                }
                            }

                            div {
                                classes = setOf("person-content")

                                h3 {
                                    classes = setOf("person-name")
                                    +extractValue(personContent, "name")
                                }

                                p {
                                    classes = setOf("person-position")
                                    +extractValue(personContent, "position")
                                }

                                p {
                                    classes = setOf("person-bio")
                                    +extractValue(personContent, "bio")
                                }

                                val socialRegex = """"social"\s*:\s*\{([^}]+)\}""".toRegex()
                                val socialContent = socialRegex.find(personContent)?.groupValues?.get(1)

                                if (socialContent != null) {
                                    div {
                                        classes = setOf("person-social")

                                        val linkedin = extractValue(socialContent, "linkedin")
                                        if (linkedin.isNotEmpty()) {
                                            a(href = linkedin) {
                                                classes = setOf("social-link", "linkedin")
                                                i {
                                                    classes = setOf("icon", "icon-linkedin")
                                                }
                                            }
                                        }

                                        val twitter = extractValue(socialContent, "twitter")
                                        if (twitter.isNotEmpty()) {
                                            a(href = twitter) {
                                                classes = setOf("social-link", "twitter")
                                                i {
                                                    classes = setOf("icon", "icon-twitter")
                                                }
                                            }
                                        }

                                        val github = extractValue(socialContent, "github")
                                        if (github.isNotEmpty()) {
                                            a(href = github) {
                                                classes = setOf("social-link", "github")
                                                i {
                                                    classes = setOf("icon", "icon-github")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper function to extract a value from a JSON string.
     */
    private fun extractValue(content: String, key: String): String {
        val regex = """"$key"\s*:\s*"([^"]+)"""".toRegex()
        val matchResult = regex.find(content)
        return matchResult?.groupValues?.get(1) ?: ""
    }
}
