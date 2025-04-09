package spock.lair.editor.ui.menu

import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import spock.lair.editor.EditorContext


@Composable
fun Run(
    run: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = run,
        modifier = modifier
    ) {
        Text(
            text = "\u25B6",
            style = MaterialTheme.typography.h5.copy(
                fontSize = 24.sp,
                color = Color.Magenta
            )
        )
    }
}


@Composable
fun StartMonitor(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Text(
            text = "\u25B6",
            style = MaterialTheme.typography.h5.copy(fontSize = 24.sp)
        )
    }
}


@Composable
fun StopMonitor(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        // Unicode ■ (stop symbol)
        Text(
            text = "\u25A0",
            style = MaterialTheme.typography.h5.copy(fontSize = 24.sp)
        )
    }
}

@Composable
fun CheckFile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Text(
            text = "\u2139",
            style = MaterialTheme.typography.h5.copy(fontSize = 24.sp)
        )
    }
}

@Composable
fun Save(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Text(
            text = "\uD83D\uDCBE", // 💾
            style = MaterialTheme.typography.h5.copy(fontSize = 24.sp)
        )
    }
}

@Composable
fun Load(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Text(
            text = "\uD83D\uDD04", // 🔄
            style = MaterialTheme.typography.h5.copy(fontSize = 24.sp)
        )
    }
}

@Composable
fun Reload(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Text(
            text = "✖",  // Unicode U+2716, a heavy multiplication 'x'
            fontSize = 24.sp,
            color = Color.White,
            style = MaterialTheme.typography.h6
        )

    }
}


@Composable
fun ThemeSwitcher(modifier: Modifier = Modifier) {
    val isDark = EditorContext.darkTheme
    IconButton(
        onClick = { EditorContext.switchTheme() },
        modifier = modifier
    ) {
        Text(
            text = if (isDark) "⚪" else "⚫",
            style = MaterialTheme.typography.h5.copy(
                fontSize = 24.sp,
            )
        )
    }
}
