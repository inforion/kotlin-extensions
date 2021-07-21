@file:Suppress("ClassName", "NOTHING_TO_INLINE")

package unsigned.literal

import unsigned.types.*

object b
object s
object u
object ul

inline operator fun Int.get(type: b) = toUByte()

inline operator fun Int.get(type: s) = toUShort()

inline operator fun Int.get(type: u) = toUInt()

inline operator fun Int.get(type: ul) = toULong()

inline operator fun Long.get(type: s) = toUShort()

inline operator fun Long.get(type: u) = toUInt()

inline operator fun Long.get(type: ul) = toULong()
