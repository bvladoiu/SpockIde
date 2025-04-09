package spock.lair

import android.content.ClipboardManager
import android.content.Context


/*
fun loadPage(name: String) {
    *//*val inputStream = try {
        SpockApp.context.assets.open(name)
    } catch (e: IOException) {*//*
        log("Resource ${name} not found in assets")
        return
    //}
    inputStream.bufferedReader().use { reader ->
        var currentTask: StringBuilder? = null
        reader.forEachLine { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isEmpty()) return@forEachLine
            if (trimmedLine.startsWith("!")) {
                currentTask?.let {
                    log(currentTask.toString())
                }
                currentTask = StringBuilder().appendLine(trimmedLine)
            } else {
                if (currentTask == null) {
                    currentTask = StringBuilder()
                }
                currentTask.appendLine(trimmedLine)
            }
        }
        currentTask?.let {
            log(currentTask.toString())
        }
    }
}*/

fun clearClipboard() {
    val clipboard = SpockApp.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.clearPrimaryClip()
}


fun getClipboardContent(): String? {
    val context = SpockApp.context
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboard.primaryClip
    return if (clipData != null && clipData.itemCount > 0) {
        clipData.getItemAt(0).text?.toString()
    } else {
        null
    }
}
