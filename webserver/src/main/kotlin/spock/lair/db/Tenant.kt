package spock.lair.db

/**
 * Enum representing the different tenants in the system.
 * Each tenant has its own database, and all tenants have access to the common database.
 */
enum class Tenant(val id: String, val dbName: String) {
    CONTADEAL("contadeal", "contadeal.db"),
    PRISMA("prisma", "prisma.db"),
    ACME("acme", "acme.db"),
    COMMON("common", "common.db");

    companion object {
        /**
         * Get the tenant from a URL path.
         * Example: "/contadeal/en/some_page" -> CONTADEAL
         * Example: "/prisma/de/home" -> PRISMA
         * If no tenant is found, returns null.
         */
        fun fromPath(path: String): Tenant? {
            val normalizedPath = path.trim('/').lowercase()
            val firstSegment = normalizedPath.split('/').firstOrNull() ?: return null

            return values().find { it.id == firstSegment }
        }
    }
}
