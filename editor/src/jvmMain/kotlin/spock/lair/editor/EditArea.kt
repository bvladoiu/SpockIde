package spock.lair.editor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp


@Composable
fun EditArea() {
    OutlinedTextField(
        value = Editor.item(),
        textStyle = TextStyle.Default.copy(fontSize = 24.sp),
        onValueChange = { Editor.setValue(it) },
        modifier = Modifier.fillMaxSize(),
        label = { Text("Editor") },
        maxLines = Int.MAX_VALUE,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default)
    )
}

