package spock.lair

/**
 * Context parameter class to house the locale and page name for scaffold DSL functions.
 * This class is used to pass locale and page name information to the scaffold DSL functions
 * when building pages.
 */
data class PageObject(
    val locale: String,
    val pageName: String
) {
    /**
     * Returns the path to the site-meta JSON file for this page.
     */
    fun getMetaPath(): String {
        return "static/site-meta/$pageName.json"
    }
    
    /**
     * Returns the full URL path for this page.
     */
    fun getUrlPath(): String {
        return "/$locale/$pageName"
    }
}