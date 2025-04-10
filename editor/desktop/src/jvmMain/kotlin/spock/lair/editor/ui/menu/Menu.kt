package spock.lair.editor.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import spock.lair.editor.EditorContext


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
            Save({ EditorContext.save() })
            Load({
                EditorContext.emit("Load")
                //EditorContext.bgScope.launch {
                //  loadSite(EditorContext.content.value)
            })
        }
        Run({
            EditorContext.content.value.lines().forEach {
                EditorContext.emit(it)
            }
        })
        Spacer(modifier = Modifier.weight(1f))
        Row {
            CheckFile(onClick = { EditorContext.emit("CheckFile") })
        }
    }
    //StopMonitor(onClick = {/* Ide.stopMonitor()*/ })
    //StartMonitor(onClick = { /*Ide.startMonitor() */ })
}

