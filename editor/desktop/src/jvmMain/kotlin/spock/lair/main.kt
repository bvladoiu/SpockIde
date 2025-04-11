package spock.lair
//:compose-jvm:jvmMain:main.kt
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import spock.lair.editor.EditArea
import spock.lair.editor.Editor
import spock.lair.editor.log.LogArea
import spock.lair.editor.menu.Menu

@Composable
fun Ide() {
    MaterialTheme {
        Editor(
            { Menu() },
            { EditArea() },
            { LogArea() }
        )
    }
}

fun main() = application {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Spock Ide"
    ) {
        Ide()
    }
}

