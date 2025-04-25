package spock.lair

import java.io.File

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
     * Returns the path to the site-meta directory for this page.
     */
    fun getMetaPath(): String {
        // Check if the page has a directory structure
        val dirPath = "static/site-meta/$pageName"
        val dir = File(dirPath)

        // If the directory exists, return the path to the directory
        if (dir.exists() && dir.isDirectory) {
            return dirPath
        }

        // Otherwise, return the path to the JSON file (for backward compatibility)
        return "static/site-meta/$pageName.json"
    }

    /**
     * Returns the path to a specific section's JSON file.
     */
    fun getSectionMetaPath(sectionName: String): String {
        val dirPath = getMetaPath()

        // If the path is a directory, return the path to the section's JSON file
        if (File(dirPath).isDirectory) {
            return "$dirPath/$sectionName.json"
        }

        // Otherwise, return the path to the page's JSON file (for backward compatibility)
        return dirPath
    }

    /**
     * Returns the full URL path for this page.
     */
    fun getUrlPath(): String {
        return "/$locale/$pageName"
    }
}
