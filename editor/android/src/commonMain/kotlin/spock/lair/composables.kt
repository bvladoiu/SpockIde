package spock.lair
//:compose-jvm:commonMain(jvm/compose shared!):composables.kt



@Composable
fun Ide() {
    MaterialTheme {
        Editor(
            header = { Menu() },
            { EditArea() },
            { LogArea() }
        )
    }
}

