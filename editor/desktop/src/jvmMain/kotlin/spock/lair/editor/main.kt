package spock.lair.editor
//:compose-jvm:jvmMain:main.kt
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = {
            ::exitApplication.invoke()
            EditorContext.close()
        },
        title = "Spock Ide"
    ) {
        Ide()
    }
}

