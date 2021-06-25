@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import ru.inforion.lab403.common.extensions.serialization.serialize
import java.io.Serializable
import java.util.*

inline fun uuid(): UUID = UUID.randomUUID()

inline fun uuid(vararg serializable: Serializable): UUID {
    val name = serializable
        .map { it.serialize(false) }
        .reduce { acc, bytes -> acc + bytes }
    return UUID.nameUUIDFromBytes(name)
}

inline fun String.toUUID(): UUID = UUID.fromString(this)