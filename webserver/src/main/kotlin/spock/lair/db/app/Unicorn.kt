package spock.lair.db.app

/**
 * Data class representing a Unicorn entity.
 * This class combines properties from both the database and JSON files.
 */
data class Unicorn(
    val id: Int? = null,
    val title: String,
    val description: String,
    val icon: String? = null,
    val index_order: Int,
    val jsonPath: String? = null
)