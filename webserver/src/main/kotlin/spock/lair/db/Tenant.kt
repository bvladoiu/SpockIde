package spock.lair.db

/**
 * Enum representing the single tenant in the system.
 * This is a simplified version that only supports the ACME tenant.
 */
enum class Tenant(val id: String, val dbName: String) {
    ACME("acme", "app.db");

    companion object {
        /**
         * Get the tenant from a URL path.
         * Since we only have one tenant, this always returns ACME.
         */
        fun fromPath(path: String): Tenant {
            return ACME
        }
    }
}
