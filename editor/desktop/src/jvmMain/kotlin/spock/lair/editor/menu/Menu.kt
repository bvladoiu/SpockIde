package spock.lair.editor.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import spock.lair.App


@Composable
fun Menu(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    )
    {
        Row {
            ThemeSwitcher()
            Save({ App.save() })
            Load({ App.load() })
        }
        Run({
           App.execute()
        })
        Spacer(modifier = Modifier.weight(1f))
        Row {
            CheckFile(onClick = { App.info() })
        }
    }
    //StopMonitor(onClick = {/* Ide.stopMonitor()*/ })
    //StartMonitor(onClick = { /*Ide.startMonitor() */ })
}

