package spock.lair.db.acme

/**
 * Data class representing a Unicorn entity.
 * This class combines properties from both the database and JSON files.
 */
data class Unicorn(
    val id: Int? = null,
    val title: String,
    val description: String,
    val icon: String? = null,
    val index_order: Int
)