package spock.lair
//:compose-jvm:commonMain(jvm/compose shared!):composables.kt

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import spock.lair.editor.ui.EditArea
import spock.lair.editor.ui.Editor
import spock.lair.editor.ui.log.LogArea
import spock.lair.editor.ui.menu.Menu


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

