package spock.lair
//:compose-jvm:jvmMain:main.kt
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import spock.lair.editor.EditorContext
import spock.lair.net.Bridge

fun main() = application {
    Window(
        onCloseRequest = {
            ::exitApplication.call()
            EditorContext.close()
            Server.stop()
            Bridge.close()
        },
        title = "Spock Ide"
    ) {
        Ide()
        Server.start()

    }
}

