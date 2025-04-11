package spock.lair.editor.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import spock.lair.App

@Composable
fun LogArea(spec: String? = null) {
    val logEntries = App.logEntries
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().wrapContentHeight()
        ) {
            items(logEntries) { entry ->
                LogEntry(text = entry)
            }
        }
    }
    LaunchedEffect(Unit) {
            //logEntries.add(0, "$it\n")
    }
}