@file:Suppress("ClassName", "NOTHING_TO_INLINE")

package unsigned.literals

import unsigned.interfaces.Unsigned
import unsigned.types.*

object b {
    inline operator fun get(value: Int) = value.toByte()
}

object s {
    inline operator fun get(value: Int) = value.toShort()
}

object i {
    inline operator fun get(value: kotlin.UInt) = value.toInt()
    inline operator fun get(value: kotlin.ULong) = value.toInt()

    inline operator fun get(value: Int) = value
    inline operator fun get(value: Long) = value.toInt()
}

object l {
    inline operator fun get(value: kotlin.UInt) = value.toLong()
    inline operator fun get(value: kotlin.ULong) = value.toLong()

    inline operator fun get(value: Int) = value.toLong()
    inline operator fun get(value: Long) = value
}

object ub {
    inline operator fun get(value: Int) = value.toUByte()
}

object us {
    inline operator fun get(value: Int) = value.toUShort()
}

object u {
    inline operator fun get(value: kotlin.UInt) = UInt(value.toInt())
    inline operator fun get(value: kotlin.ULong) = UInt(value.toInt())

    inline operator fun get(value: Int) = value.toUInt()
    inline operator fun get(value: Long) = value.toUInt()
}

object ul {
    inline operator fun get(value: kotlin.UInt) = ULong(value.toLong())
    inline operator fun get(value: kotlin.ULong) = ULong(value.toLong())

    inline operator fun get(value: Int) = value.toULong()
    inline operator fun get(value: Long) = value.toULong()
}

inline val Ib get() = UByte(1)
inline val Ob get() = UByte(0)

inline val Is get() = UShort(1)
inline val Os get() = UShort(0)

inline val I get() = UInt(1)
inline val O get() = UInt(0)

inline val Il get() = ULong(1)
inline val Ol get() = ULong(0)