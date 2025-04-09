package spock.lair.strings

/**
 *   ![type.style](id "Name has Spaces")
 *   prop: value
 *   .state: props
 *
 */

fun String.type(): String {
    val regex = Regex("""(?:!\[|\[)([A-Za-z0-9]+)""")
    return regex.find(this)?.groupValues?.get(1)?.trim() ?: ""
}


fun String.style(): String {
    val regex = Regex("""(?:!\[|\[)[A-Za-z0-9]+\.(\w+)(?=[:\]])""")
    return regex.find(this)?.groupValues?.get(1) ?: "default"
}

fun String.id(): String {
    val regex = Regex("""\(([A-Za-z0-9_]+)""")
    return regex.find(this)?.groupValues?.get(1) ?: ""
}

fun String.key(): String {
    return "${type()}:${style()}:${id()}"
}

fun String.name(): String {
    val regex = Regex(""""([^"]+)"""")
    return regex.find(this)?.groupValues?.get(1) ?: ""
}

fun String.prop(name: String): String? {
    val regex = Regex("(?m)^\\s*([^:\\s]+)\\s*:\\s*(.*)$")
    return regex.findAll(this)
        .firstOrNull { it.groupValues[1].equals(name, ignoreCase = true) }
        ?.groupValues?.get(2)?.trim()
}


fun String.state(): String {
    val regex = Regex("""^\s*\.[A-Za-z0-9_-]+\s*:\s*(.+)$""")
    val match = regex.find(this)?.groupValues?.get(1)?.trim() ?: return ""
    return match.split(",").first().trim()
}




fun String.props(): Map<String, String> {
    val regex = Regex("(?m)^\\s*([^:.\\s][^:]*)\\s*:\\s*(.*)$") // Key starts !., no spaces, allow spaces within
    return regex.findAll(this)
        .associate { it.groupValues[1].trim() to it.groupValues[2].trim() }
}

fun String.states(): Map<String, Map<String, String>> {
    val stateRegex = Regex("(?m)^\\s*\\.([^:]+):\\s*(.*)$")
    return stateRegex.findAll(this).associate { matchResult ->
        val stateName = matchResult.groupValues[1].trim()
        val propsString = matchResult.groupValues[2].trim()
        val stateProps = propsString.split(',')
            .mapNotNull { propPair ->
                propPair.split('=', limit = 2)
                    .takeIf { it.size == 2 }
                    ?.let { it[0].trim() to it[1].trim() }
            }.toMap()
        stateName to stateProps
    }
}

fun String.path(): String? {
    return this.prop("path")
}

fun String.file(): String? {
    return this.prop("file")
}