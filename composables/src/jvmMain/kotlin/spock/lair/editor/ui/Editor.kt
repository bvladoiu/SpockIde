package spock.lair.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
//import java.io.File


@Composable
fun Editor(
    header: @Composable () -> Unit,
    left: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        header()
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val totalWidthPx = with(density) { maxWidth.toPx() }
            var leftWeight by remember { mutableStateOf(0.5f) }

            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(leftWeight).fillMaxHeight()) {
                    left()
                }
                Box(
                    modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Gray).draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            val fraction = delta / totalWidthPx
                            leftWeight = (leftWeight + fraction).coerceIn(0f, 1f)
                        })
                )
                Box(modifier = Modifier.weight(1f - leftWeight).fillMaxHeight()) {
                    right()
                }
            }
        }
    }
}