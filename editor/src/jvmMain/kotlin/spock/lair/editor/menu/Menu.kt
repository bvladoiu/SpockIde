package spock.lair.editor.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import spock.lair.App
import spock.lair.App.log


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
            Load({ App.load() })
            CheckFile({ App.info() })
            Clear({ App.info() })
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Save({ App.save() })
            Run({
                log("Proxy : TODO")
                App.run() })
        }
    }
    //StopMonitor(onClick = {/* Ide.stopMonitor()*/ })
    //StartMonitor(onClick = { /*Ide.startMonitor() */ })
}

