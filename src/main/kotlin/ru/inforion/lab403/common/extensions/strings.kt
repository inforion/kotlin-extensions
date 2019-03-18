@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import kotlin.experimental.and

/**
 * Created by Alexei Gladkikh on 20/06/16.
 *
 * Extension methods and function for working with Strings and transformation
 */
operator fun String.get(range: IntRange): String {
    val _r = if (range.last < 0) range.start..this.length + range.last else range
    return this.slice(_r.start until _r.last)
}

fun String.toCamelCase(): String {
    if (this.isNotEmpty())
        return this[0].toLowerCase() + this.substring(1)
    return this
}

fun ByteArray.ascii(): String = map { if (it >= 0x20 && it < 0x7F) it.toChar() else '.' }.joinToString("")

fun ByteArray.hexlify(upperCase: Boolean = true, separator: Char? = null): String {
    val hexChars = CharArray(this.size * 2)
    for (j in 0 until this.size) {
        val v = this[j].toInt()
        hexChars[j * 2] = Character.forDigit(v[7..4], 16)
        hexChars[j * 2 + 1] = Character.forDigit(v[3..0], 16)
    }
    val result = if (upperCase) String(hexChars).toUpperCase() else String(hexChars)
    if (separator != null) {
        val builder = StringBuilder()
        builder.append(result[0..2])
        for (i in 2 until result.length step 2) {
            builder.append(separator)
            builder.append(result[i..i + 2])
        }
        return builder.toString()
    }
    return result
}

fun String.unhexlify(): ByteArray {
    val tmp = this.replace(" ", "")
    val data = ByteArray(tmp.length / 2)
    for (k in 0 until tmp.length step 2) {
        val ch0 = Character.digit(tmp[k + 0], 16)
        val ch1 = Character.digit(tmp[k + 1], 16)
        data[k / 2] = (ch0.shl(4) or ch1).toByte()
    }
    return data
}

fun Long.sbits(bitSize: Int): String = (bitSize - 1 downTo 0).joinToString("") { "${this[it]}" }

val Long.sbits get() = sbits(64)
val Int.sbits get() = asLong.sbits(32)
val Short.sbits get() = asLong.sbits(16)
val Byte.sbits get() = asLong.sbits(8)

fun ByteArray.decode(charset: Charset = Charsets.UTF_8): String {
    var size = 0
    while (this[size].toInt() != 0) size++
    return String(this, 0, size, charset)
}

fun String.toUnhexlifyByteBuffer(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteBuffer {
    return ByteBuffer.wrap(this.unhexlify()).apply { order(byteOrder) }
}

fun String.toLong(radix: Int = 10): Long = java.lang.Long.parseLong(this, radix)
fun String.toULong(radix: Int = 10): Long = java.lang.Long.parseUnsignedLong(this, radix)
fun String.toInt(radix: Int = 10): Int = Integer.parseInt(this, radix)
fun String.toUInt(radix: Int = 10): Int = Integer.parseUnsignedInt(this, radix)
fun String.toShort(radix: Int = 10): Short = java.lang.Short.parseShort(this, radix)
fun String.toByte(radix: Int = 10): Byte = java.lang.Byte.parseByte(this, radix)


val String.hexAsInt get() = java.lang.Integer.parseInt(this, 16)
val String.hexAsUInt get() = java.lang.Integer.parseUnsignedInt(this, 16)
val String.hexAsULong get() = java.lang.Long.parseUnsignedLong(this, 16)


@Deprecated("Use property syntax", replaceWith = ReplaceWith("hexAsInt"))
fun String.toIntFromHex(): Int = Integer.parseInt(this, 16)

@Deprecated("Use property syntax", replaceWith = ReplaceWith("hexAsUInt"))
fun String.toUIntFromHex(): Int = Integer.parseUnsignedInt(this, 16)

@Deprecated("Use property syntax", replaceWith = ReplaceWith("hexAsULong"))
fun String.toULongFromHex(): Long = java.lang.Long.parseUnsignedLong(this, 16)

@Deprecated("Use hex1, hex2, hex4 or hex8 properties instead", replaceWith = ReplaceWith("hex4"))
fun Number.toHexString(): String = "%08X".format(this)


infix fun String.resembles(other: String): Boolean {
    return this.asSequence().zip(other.asSequence()).filter { it.first != '*' }.all { it.first == it.second }
}

/**
 * Returns string of hex representation of byte value with only 2 tetrades
 */
inline val Byte.hex2 get() = "%02X".format(this)

/**
 * Returns string of hex representation of short value with only 2 tetrades
 */
inline val Short.hex2 get() = "%02X".format(this and 0xFF)
/**
 * Returns string of hex representation of short value with only 4 tetrades
 */
inline val Short.hex4 get() = "%04X".format(this and -1)

/**
 * Returns string of hex representation of int value with only 2 tetrades
 */
inline val Int.hex2 get() = "%02X".format(this and 0xFF)
/**
 * Returns string of hex representation of int value with only 4 tetrades
 */
inline val Int.hex4 get() = "%04X".format(this and 0xFFFF)
/**
 * Returns string of hex representation of int value with only 8 tetrades
 */
inline val Int.hex8 get() = "%08X".format(this and -1)

/**
 * Returns string of hex representation of long value with only 2 tetrades
 */
inline val Long.hex2 get() = "%02X".format(this and 0xFF)
/**
 * Returns string of hex representation of long value with only 4 tetrades
 */
inline val Long.hex4 get() = "%04X".format(this and 0xFFFF)
/**
 * Returns string of hex representation of long value with only 8 tetrades
 */
inline val Long.hex8 get() = "%08X".format(this and 0xFFFF_FFFF)
/**
 * Returns string of hex representation of long value with only 16 tetrades
 */
inline val Long.hex16 get() = "%016X".format(this)

/**
 * Returns lower-case string of hex representation of byte value with only 2 tetrades
 */
inline val Byte.lhex2 get() = "%02x".format(this)

/**
 * Returns lower-case string of hex representation of short value with only 2 tetrades
 */
inline val Short.lhex2 get() = "%02x".format(this and 0xFF)
/**
 * Returns lower-case string of hex representation of short value with only 4 tetrades
 */
inline val Short.lhex4 get() = "%04x".format(this and -1)

/**
 * Returns lower-case string of hex representation of int value with only 2 tetrades
 */
inline val Int.lhex2 get() = "%02x".format(this and 0xFF)
/**
 * Returns lower-case string of hex representation of int value with only 4 tetrades
 */
inline val Int.lhex4 get() = "%04x".format(this and 0xFFFF)
/**
 * Returns lower-case string of hex representation of int value with only 8 tetrades
 */
inline val Int.lhex8 get() = "%08x".format(this and -1)

/**
 * Returns lower-case string of hex representation of long value with only 2 tetrades
 */
inline val Long.lhex2 get() = "%02x".format(this and 0xFF)
/**
 * Returns lower-case string of hex representation of long value with only 4 tetrades
 */
inline val Long.lhex4 get() = "%04x".format(this and 0xFFFF)
/**
 * Returns lower-case string of hex representation of long value with only 8 tetrades
 */
inline val Long.lhex8 get() = "%08x".format(this and 0xFFFF_FFFF)
/**
 * Returns lower-case string of hex representation of long value with only 16 tetrades
 */
inline val Long.lhex16 get() = "%016x".format(this)

inline val Long.str get() = toString()
inline val Int.str get() = toString()
inline val Short.str get() = toString()
inline val Byte.str get() = toString()


inline val Long.hex get() = when {
    this > 0xFFFF_FFFF -> hex16
    this > 0xFFFF -> hex8
    this > 0xFF -> hex4
    else -> hex2
}

inline val Int.hex get() = when {
    this > 0xFFFF -> hex8
    this > 0xFF -> hex4
    else -> hex2
}

inline val Short.hex get() = when {
    this > 0xFF -> hex4
    else -> hex2
}

inline val Byte.hex get() = hex2

/**
 * Slice input string from 0 to maxlen.
 * If maxlen > string.length then remain string.length (no exception thrown)
 */
inline fun String.stretch(maxlen: Int, alignRight: Boolean = true): String {
    if (length == 0) return "%${maxlen}s".format(this)

    val tmp: String
    val format: String
    if (!alignRight) {
        tmp = slice(Math.max(0, length - maxlen)..(length - 1))
        format = "%${maxlen}s"
    } else {
        tmp = slice(0..Math.min(maxlen - 1, length - 1))
        format = "%-${maxlen}s"
    }
    return format.format(tmp)
}

val emptyString: String = String()