package spock.lair.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun Editor(
    header: @Composable () -> Unit,
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        header()
        val splitPaneState = rememberSplitPaneState(initialPositionPercentage = 0.3f)
        HorizontalSplitPane(
            splitPaneState = splitPaneState,
            modifier = Modifier.fillMaxSize().background(Color.LightGray)
        ) {
            first(minSize = 100.dp) { // Minimum width for the left pane
                Box(modifier = Modifier.fillMaxSize()) {
                    leftContent()
                }
            }
            second(minSize = 150.dp) { // Minimum width for the right pane
                Box(modifier = Modifier.fillMaxSize()) {
                    rightContent()
                }
            }
        }
    }
}

