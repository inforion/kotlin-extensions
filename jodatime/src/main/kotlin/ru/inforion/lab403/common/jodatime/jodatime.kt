@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.jodatime

import org.joda.time.DateTime
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

inline fun DataOutputStream.writeDateTime(timestamp: DateTime) = writeLong(timestamp.millis)

inline fun DataInputStream.readDateTime(): DateTime = DateTime(readLong())

inline fun Date.toDateTime(): DateTime = DateTime(time)