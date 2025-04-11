package spock.lair.editor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import spock.lair.App


@Composable
fun EditArea() {
    OutlinedTextField(
        value = App.content.value,
        onValueChange = { App.content.value = it },
        modifier = Modifier.fillMaxSize(),
        label = { Text("Editor") },
        maxLines = Int.MAX_VALUE,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default)
    )
}

