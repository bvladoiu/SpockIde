package spock.lair.editor.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.microsoft.playwright.Browser
import kotlinx.coroutines.delay
import spock.lair.Spock
import spock.lair.editor.Editor
import spock.lair.editor.Editor.checkFile
import spock.lair.editor.Editor.message
import java.io.File


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
            Load({ Editor.load() })
            CheckFile({ Editor.checkFile(Editor.item()) })
            Clear({ Editor.clear() })
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            LaunchedEffect(key1 = Unit) {
                delay(6000)
                message.value = ""
            }
            if (message.value.isNotBlank()) {
                Text(
                    message.value,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Save({ Editor.save() })
//            Run({ Browser.(Editor.item()) })
        }
    }

}

