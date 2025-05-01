package spock.lair.data

data class Data(
    val id: String, // the json file name without '.json'
    val props: Map<String, String>
)