@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.logging.formatters

import ru.inforion.lab403.common.extensions.stretch
import ru.inforion.lab403.common.logging.Caller
import ru.inforion.lab403.common.logging.abbreviation
import ru.inforion.lab403.common.logging.logger.Record
import java.text.SimpleDateFormat
import java.util.*

class Informative(
    val painter: Formatter = ColorMultiline,
    val messageFormat: String = defaultMessageFormat
) :
    Formatter {

    companion object {
        private const val STACK_TRACE_CALLER_INDEX = 4
        var locationLength = 50
        var nameLength = 20
        var dateFormat = "HH:mm:ss"
        var defaultMessageFormat = "%(time) %(level) %(location): %(message)\n"

        var extremelyInformativeMessageFormat = "%(time) %(level) [%(name)][%(thread)] %(location): %(message)\n"
        var newInformativeMessageFormat = "%(time) %(level) [%(name)] [%(thread)]: %(message)\n"
    }

    private inline fun stretch(string: String, maxlen: Int) = if (string.length <= maxlen)
        string.stretch(maxlen, false)
    else {
        val stretched = string.stretch(maxlen - 3, false)
        "...$stretched"
    }

    private inline fun formatLocation(caller: Caller): String {
        // TODO: Wait while JB fix wrong regex pattern for stack trace element in console
        //   see parseStackTraceLine in KotlinExceptionFilter.kt at Kotlin repo
        val location = caller.toString()
        return if (locationLength != -1) stretch(location, locationLength) else location
    }

    private inline fun formatDate(millis: Long) = SimpleDateFormat(dateFormat).format(Date(millis))

    override fun format(message: String, record: Record): String {
        val level = record.level.abbreviation
        val time = record.millis?.let { formatDate(it) } ?: "[No time]"
        val name = stretch(record.logger.name, nameLength)
        val thread = record.thread?.name ?: "[No thread]"

        // TODO: Make more efficient way
        val formatted = messageFormat
            .replace("%(time)", time)
            .replace("%(level)", level)
            .replace("%(name)", name)
            .replace("%(thread)", thread)
            .replace("%(message)", message)
            .let {
                record.thread?.let { thread ->
                    if ("%(location)" in it) {
                        val trace = thread.stackTrace
                        val index = STACK_TRACE_CALLER_INDEX + record.stackFrameIndex
                        it.replace(
                            "%(location)", formatLocation(
                                thread.stackTrace[
                                    if (index in trace.indices) index
                                    else trace.lastIndex
                                ]
                            )
                        )
                    } else it
                } ?: it
            }
        return painter.format(formatted, record)
    }
}