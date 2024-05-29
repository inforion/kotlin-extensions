package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.stretch
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(millis: Long, format: String) =
    SimpleDateFormat(format).format(Date(millis))

inline fun stretchInline(string: String, maxlen: Int, appendCallback: (String) -> Unit) {
    if (string.length <= maxlen)
        appendCallback(string.stretch(maxlen, false))
    else {
        appendCallback("...")
        appendCallback(string.stretch(maxlen - 3, false))
    }
}

fun stretch(string: String, maxlen: Int): String {
    if (string.length <= maxlen)
        return string.stretch(maxlen, false)
    else {
        return "..." + string.stretch(maxlen - 3, false)
    }
}
