@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.json

import java.io.InputStream

// Other json helpers

@PublishedApi internal val commentsRegex = Regex("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)")

inline fun String.removeJsonComments() = replace(commentsRegex, " ")

inline fun InputStream.removeJsonComments() = bufferedReader().readText().removeJsonComments()