package spock.lair.editor.ui.log

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun LogEntry(text: String) {
    // val primaryColor = MaterialTheme.colors.primary
    val highlightedText = highlightLogEntry(
        Color.Red,
        Color.Blue,
        Color.Magenta,
        text
    )
    var textFieldState = remember { mutableStateOf(TextFieldValue(text = highlightedText.text)) }
    BasicTextField(
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        readOnly = true,
        visualTransformation = VisualTransformation { input ->
            val highlighted: AnnotatedString = highlightLogEntry(
                Color.Red,
                Color.Blue,
                Color.Magenta,
                input.text
            )
            TransformedText(highlighted, OffsetMapping.Identity)
        }
    )
}

fun highlightLogEntry(
    type: Color,
    variant: Color,
    id: Color,
    text: String
): AnnotatedString {
    val builder = AnnotatedString.Builder(text)

    val typeVal = text.type()
    if (typeVal.isNotEmpty()) {
        val start = text.indexOf(typeVal)
        if (start != -1) {
            builder.addStyle(SpanStyle(color = type), start, start + typeVal.length)
        }
    }

    val variantVal = text.style()
    if (variantVal.isNotEmpty()) {
        val start = text.indexOf(variantVal)
        if (start != -1) {
            builder.addStyle(SpanStyle(color = variant), start, start + variantVal.length)
        }
    }

    val idVal = text.id()
    if (idVal.isNotEmpty()) {
        val start = text.indexOf(idVal)
        if (start != -1) {
            builder.addStyle(SpanStyle(color = id), start, start + idVal.length)
        }
    }

    return builder.toAnnotatedString()
}
