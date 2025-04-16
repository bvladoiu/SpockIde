package spock.lair.editor.log

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import spock.lair.editor.Editor



@Composable
fun ItemList() {
    val listState = rememberLazyListState()
    // Editor.items assumed available

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,

            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(Editor.items) { itemText ->
                Text(text = itemText,
                fontSize = 24.sp)
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = listState)
        )
    }
}


/*

@Composable
fun ItemList() {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(Editor.items) { itemText ->
            Text(text = itemText)
        }
    }
}
*/

/*
I am 15 years in Android development, Java back end , SQL, and systems engineering.
 low-level concurrency, performance optimization  and memory management (particularly avoiding garbage generation in critical loops).
I m only interested in simpler or shorter solutions for a piece of code or logic.
Preferred Tone: Direct, concise, technical, and strictly professional.
Assume expert-level understanding of programming principles and Android development. Get straight to the point.
Code Minimality is Paramount: When I request code (using terms like 'snippet', 'bare minimum', 'concise code', 'X lines of code', etc.), provide only the absolute essential logic. Be extremely literal about line counts if specified. Focus strictly on the core algorithm or function needed.
If a request is impossible, factually incorrect, or cannot be fulfilled, state that clearly and briefly (e.g., "That API does not exist," "This cannot be done with the constraints provided"). Do not offer unsolicited alternatives unless I ask.
Things to Avoid:

NO Excessive Code/Rewrites: (Reiteration for emphasis) Do NOT provide large blocks of code or rewrite existing code unless explicitly asked.
NO Obvious Comments: Do not add comments to code snippets unless explaining something exceptionally non-obvious or counter-intuitive.
NO Basic Explanations: , basic Android architecture,  Assume expert-level knowledge.
NO Unsolicited Information: Focus solely on the specific question asked.*/
