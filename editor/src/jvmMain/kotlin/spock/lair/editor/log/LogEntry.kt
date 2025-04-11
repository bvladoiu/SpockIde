import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp


@Composable
fun ListEntry(text: String) {
    val highlightedText = remember(text) {
        highlightLogEntry(Color.Red, Color.Blue, Color.Magenta, text)
    }
    // val textFieldState = remember { mutableStateOf(TextFieldValue(annotatedString = highlightedText)) }
    // val isEditing = remember { mutableStateOf(false) }
    // val coroutineScope = rememberCoroutineScope()

    // Replace TextField with Text
    Text(
        text = highlightedText, // Use highlightedText directly
        modifier = Modifier.fillMaxWidth(),
        style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

fun highlightLogEntry(
    type: Color,
    variant: Color,
    id: Color,
    text: String
): AnnotatedString {
    val builder = AnnotatedString.Builder(text)

    val typeVal = "" //text.type()
    if (typeVal.isNotEmpty()) {
        val start = text.indexOf(typeVal)
        if (start != -1) {
            builder.addStyle(SpanStyle(color = type), start, start + typeVal.length)
        }
    }

    val variantVal = "" //text.style()
    if (variantVal.isNotEmpty()) {
        val start = text.indexOf(variantVal)
        if (start != -1) {
            builder.addStyle(SpanStyle(color = variant), start, start + variantVal.length)
        }
    }

    val idVal = "" //text.id()
    if (idVal.isNotEmpty()) {
        val start = text.indexOf(idVal)
        if (start != -1) {
            builder.addStyle(SpanStyle(color = id), start, start + idVal.length)
        }
    }

    return builder.toAnnotatedString()
}