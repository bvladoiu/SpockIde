package spock.lair
//:compose-jvm:jvmMain:main.kt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.microsoft.playwright.Playwright
import spock.lair.editor.EditArea
import spock.lair.editor.Editor.darkTheme
import spock.lair.editor.Editor.pwd
import spock.lair.editor.EditorScreen
import spock.lair.editor.log.ItemList
import spock.lair.editor.menu.Menu
import spock.lair.fileio.Storage
import spock.lair.tools.playwright.Browser

@Composable
fun Ide() {
    val colors = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
    MaterialTheme(colorScheme = colors, shapes = shapes) {
        EditorScreen(
            { Menu() },
            { ItemList() },
            { EditArea() }
        )
    }
}


fun main() {
     cli()
    //ui()
}

public class Scraper {
    fun main(){
        //https://www.marqeta.com/platform/applications
        }
}


fun ui() = application {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Spock Ide"
    ) {
        Ide()
        Browser.cli("![browser](Playwright )")
    }
}


fun cli() {
    println(
        "" +
                "n" +
                "${System.getProperty("user.dir")}\n" +
                "${Storage.site()}\n" +
                "${Storage.local()}\n" +
                "${Storage.public()}" +
                " ${Storage.save("[type.subtype](accurate)")}\" +" +
                "" +
                ""
    )
}


