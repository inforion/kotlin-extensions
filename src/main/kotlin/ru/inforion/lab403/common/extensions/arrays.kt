@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.DataInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ByteOrder.BIG_ENDIAN
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.nio.charset.Charset
import kotlin.math.abs

/**
 * Safely convert byte array to string without loss any information (even negative bytes)
 *
 * Безопасно конвертирует массив байт в строку с возможностью обратного преобразования
 */
inline fun ByteArray.convertToString() = toString(Charsets.ISO_8859_1)

/**
 * Safely convert string to byte array without loss any information (even negative bytes)
 *
 * Безопасно конвертирует строку в массив байт с возможностью обратного преобразования
 */
inline fun String.convertToBytes() = toByteArray(Charsets.ISO_8859_1)

fun ByteArray.fromPDP11(start: Int = 0, end: Int = 0): ByteArray {
    val result = this.slice(start until end).toByteArray()
    for (k in result.indices step 2) {
        val tmp = result[k]
        result[k] = result[k + 1]
        result[k + 1] = tmp
    }
    return result
}

fun <T> Array<T>.bisectLeft(key: T): Int where T : Comparable<T> {
    var idx = abs(sorted().binarySearch(key)).coerceAtMost(size - 1)
    while (idx > 0 && this[idx - 1] >= key) idx--
    return idx
}

inline operator fun <reified T> Array<T>.get(range: IntRange): Array<T> {
    val size = if (range.last <= 0) {
        this.size - range.first - range.last
    } else {
        range.last - range.first + 1
    }
    return Array(size) { this[(it + range.first) % this.size] }
}

operator fun ByteArray.get(range: IntRange): ByteArray {
    val size = if (range.last <= 0) {
        this.size - range.first - range.last
    } else {
        range.last - range.first + 1
    }

    val result = ByteArray(size)

    if (range.last < 0) {
        val s1 = this.size - range.first
        val s2 = -range.last
        System.arraycopy(this, range.first, result, 0, s1)
        System.arraycopy(this, 0, result, s1, s2)
    } else {
        System.arraycopy(this, range.first, result, 0, result.size)
    }

    return result
}

fun ByteArray.startswith(data: ByteArray, size: Int = -1): Boolean {
    require(size <= data.size) { "size must be <= data.size" }
    if (size != -1) {
        if (size > this.size)
            return false
    } else {
        if (data.size > this.size)
            return false
    }
    val last = (if (size != -1) size else data.size) - 1
    for (k in 0..last) {
        if (this[k] != data[k])
            return false
    }
    return true
}

fun ByteArray.fuzzyStartswith(data: ByteArray, size: Int = -1, fuzzy: List<Boolean>): Boolean {
    require(size <= data.size) { "size must be <= data.size" }
    if (size != -1) {
        if (size > this.size)
            return false
    } else {
        if (data.size > this.size)
            return false
    }
    val last = (if (size != -1) size else data.size) - 1
    for (k in 0..last)
        if (!fuzzy[k] && this[k] != data[k])
            return false
    return true
}

inline fun ByteArray.indexOf(element: Byte, fromIndex: Int): Int {
    for (index in fromIndex..size)
        if (element == this[index])
            return index
    return -1
}

inline fun ByteArray.copyOfRange(start: Int) = copyOfRange(start, size)

inline fun <T> ByteArray.split(byte: Byte, limit: Int = 0, modify: (ByteArray) -> T): List<T> {
    require(limit >= 0) { "Limit must be non-negative, but was $limit." }

    var currentOffset = 0
    var nextIndex = indexOf(byte, currentOffset)
    if (nextIndex == -1 || limit == 1) {
        return listOf(modify(this.copyOf()))
    }

    val isLimited = limit > 0
    val result = ArrayList<T>(if (isLimited) limit.coerceAtMost(10) else 10)
    do {
        result.add(modify(copyOfRange(currentOffset, nextIndex)))
        currentOffset = nextIndex + 1
        // Do not search for next occurrence if we're reaching limit
        if (isLimited && result.size == limit - 1) break
        nextIndex = indexOf(byte, currentOffset)
    } while (nextIndex != -1)

    result.add(modify(copyOfRange(currentOffset)))
    return result
}

inline fun <T> ByteArray.split(char: Char, limit: Int = 0, modify: (ByteArray) -> T) =
    split(char.code.toByte(), limit, modify)

inline fun ByteArray.split(char: Char, limit: Int = 0) = split(char.code.toByte(), limit) { it }

fun ByteArray.chunks(length: Int): List<ByteArray> {
    var offset = 0
    val dis = DataInputStream(this.inputStream())
    return List(this.size / length + 1) {
        val size = Math.min(dis.available(), length)
        val buffer = ByteArray(size)
        offset += dis.read(buffer, 0, size)
        buffer
    }
}

inline val ByteBuffer.byte: Byte get() = get()
inline fun ByteBuffer.byte(value: Byte): ByteBuffer = put(value)
inline fun ByteBuffer.short(value: Short): ByteBuffer = putShort(value)
inline fun ByteBuffer.int(value: Int): ByteBuffer = putInt(value)
inline fun ByteBuffer.long(value: Long): ByteBuffer = putLong(value)

operator fun <T, E : Enum<E>> Array<T>.get(e: Enum<E>): T = this[e.ordinal]
operator fun <T, E : Enum<E>> Array<T>.set(e: Enum<E>, value: T) {
    this[e.ordinal] = value
}

fun ByteArray.putInt64(index: Int, value: Long, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[63..56].byte
            this[index + 1] = value[55..48].byte
            this[index + 2] = value[47..40].byte
            this[index + 3] = value[39..32].byte
            this[index + 4] = value[31..24].byte
            this[index + 5] = value[23..16].byte
            this[index + 6] = value[15..8].byte
            this[index + 7] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 7] = value[63..56].byte
            this[index + 6] = value[55..48].byte
            this[index + 5] = value[47..40].byte
            this[index + 4] = value[39..32].byte
            this[index + 3] = value[31..24].byte
            this[index + 2] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt56(index: Int, value: Long, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[55..48].byte
            this[index + 1] = value[47..40].byte
            this[index + 2] = value[39..32].byte
            this[index + 3] = value[31..24].byte
            this[index + 4] = value[23..16].byte
            this[index + 5] = value[15..8].byte
            this[index + 6] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 6] = value[55..48].byte
            this[index + 5] = value[47..40].byte
            this[index + 4] = value[39..32].byte
            this[index + 3] = value[31..24].byte
            this[index + 2] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt48(index: Int, value: Long, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[47..40].byte
            this[index + 1] = value[39..32].byte
            this[index + 2] = value[31..24].byte
            this[index + 3] = value[23..16].byte
            this[index + 4] = value[15..8].byte
            this[index + 5] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 5] = value[47..40].byte
            this[index + 4] = value[39..32].byte
            this[index + 3] = value[31..24].byte
            this[index + 2] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt40(index: Int, value: Long, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[39..32].byte
            this[index + 1] = value[31..24].byte
            this[index + 2] = value[23..16].byte
            this[index + 3] = value[15..8].byte
            this[index + 4] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 4] = value[39..32].byte
            this[index + 3] = value[31..24].byte
            this[index + 2] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt32(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[31..24].byte
            this[index + 1] = value[23..16].byte
            this[index + 2] = value[15..8].byte
            this[index + 3] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 3] = value[31..24].byte
            this[index + 2] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt24(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 2] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 2] = value[23..16].byte
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt16(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[15..8].byte
            this[index + 1] = value[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 1] = value[15..8].byte
            this[index + 0] = value[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt8(index: Int, value: Int) {
    this[index + 0] = value[7..0].byte
}

fun ByteArray.putInt(index: Int, value: Long, size: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (size) {
        1 -> putInt8(index, value.int)
        2 -> putInt16(index, value.int, order)
        3 -> putInt24(index, value.int, order)
        4 -> putInt32(index, value.int, order)
        5 -> putInt40(index, value, order)
        6 -> putInt48(index, value, order)
        7 -> putInt56(index, value, order)
        8 -> putInt64(index, value, order)
        else -> throw IllegalArgumentException("Wrong int size!")
    }
}

fun ByteArray.getInt64(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 7].ulong, 7..0)
        .insert(this[index + 6].ulong, 15..8)
        .insert(this[index + 5].ulong, 23..16)
        .insert(this[index + 4].ulong, 31..24)
        .insert(this[index + 3].ulong, 39..32)
        .insert(this[index + 2].ulong, 47..40)
        .insert(this[index + 1].ulong, 55..48)
        .insert(this[index + 0].ulong, 63..56)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
        .insert(this[index + 4].ulong, 39..32)
        .insert(this[index + 5].ulong, 47..40)
        .insert(this[index + 6].ulong, 55..48)
        .insert(this[index + 7].ulong, 63..56)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt56(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 6].ulong, 7..0)
        .insert(this[index + 5].ulong, 15..8)
        .insert(this[index + 4].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
        .insert(this[index + 2].ulong, 39..32)
        .insert(this[index + 1].ulong, 47..40)
        .insert(this[index + 0].ulong, 55..48)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
        .insert(this[index + 4].ulong, 39..32)
        .insert(this[index + 5].ulong, 47..40)
        .insert(this[index + 6].ulong, 55..48)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt48(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 5].ulong, 7..0)
        .insert(this[index + 4].ulong, 15..8)
        .insert(this[index + 3].ulong, 23..16)
        .insert(this[index + 2].ulong, 31..24)
        .insert(this[index + 1].ulong, 39..32)
        .insert(this[index + 0].ulong, 47..40)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
        .insert(this[index + 4].ulong, 39..32)
        .insert(this[index + 5].ulong, 47..40)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt40(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 4].ulong, 7..0)
        .insert(this[index + 3].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 1].ulong, 31..24)
        .insert(this[index + 0].ulong, 39..32)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
        .insert(this[index + 4].ulong, 39..32)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}


fun ByteArray.getInt32(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 3].ulong, 7..0)
        .insert(this[index + 2].ulong, 15..8)
        .insert(this[index + 1].ulong, 23..16)
        .insert(this[index + 0].ulong, 31..24)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt24(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 2].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 0].ulong, 23..16)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt16(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 1].ulong, 7..0)
        .insert(this[index + 0].ulong, 15..8)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt8(index: Int) = this[index].uint mask 8

@Deprecated("getValue is deprecated use getInt instead", ReplaceWith("getInt"))
fun ByteArray.getValue(index: Int, bytesNumber: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (bytesNumber) {
    1 -> getInt8(index).ulong mask 8
    2 -> getInt16(index, order)
    3 -> getInt24(index, order)
    4 -> getInt32(index, order)
    8 -> getInt64(index, order)
    else -> throw IllegalArgumentException("Available bytesNumber is 1, 2, 3, 4, 8")
}

fun ByteArray.getDouble(index: Int, order: ByteOrder = LITTLE_ENDIAN): Double = when (order) {
    LITTLE_ENDIAN -> insert(this[index + 7].ulong, 7..0)
        .insert(this[index + 6].ulong, 15..8)
        .insert(this[index + 5].ulong, 23..16)
        .insert(this[index + 4].ulong, 31..24)
        .insert(this[index + 3].ulong, 39..32)
        .insert(this[index + 2].ulong, 47..40)
        .insert(this[index + 1].ulong, 55..48)
        .insert(this[index + 0].ulong, 63..56)
        .ieee754()
    BIG_ENDIAN -> insert(this[index + 0].ulong, 7..0)
        .insert(this[index + 1].ulong, 15..8)
        .insert(this[index + 2].ulong, 23..16)
        .insert(this[index + 3].ulong, 31..24)
        .insert(this[index + 4].ulong, 39..32)
        .insert(this[index + 5].ulong, 47..40)
        .insert(this[index + 6].ulong, 55..48)
        .insert(this[index + 7].ulong, 63..56)
        .ieee754()
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

fun ByteArray.getInt(index: Int, size: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (size) {
    1 -> getInt8(index).ulong mask 8
    2 -> getInt16(index, order)
    3 -> getInt24(index, order)
    4 -> getInt32(index, order)
    5 -> getInt40(index, order)
    6 -> getInt48(index, order)
    7 -> getInt56(index, order)
    8 -> getInt64(index, order)
    else -> throw IllegalArgumentException("Wrong int size!")
}

fun ByteArray.putDouble(index: Int, value: Double, order: ByteOrder = LITTLE_ENDIAN) {
    val tmp = value.ieee754AsSigned()
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = tmp[63..56].byte
            this[index + 1] = tmp[55..48].byte
            this[index + 2] = tmp[47..40].byte
            this[index + 3] = tmp[39..32].byte
            this[index + 4] = tmp[31..24].byte
            this[index + 5] = tmp[23..16].byte
            this[index + 6] = tmp[15..8].byte
            this[index + 7] = tmp[7..0].byte
        }
        LITTLE_ENDIAN -> {
            this[index + 7] = tmp[63..56].byte
            this[index + 6] = tmp[55..48].byte
            this[index + 5] = tmp[47..40].byte
            this[index + 4] = tmp[39..32].byte
            this[index + 3] = tmp[31..24].byte
            this[index + 2] = tmp[23..16].byte
            this[index + 1] = tmp[15..8].byte
            this[index + 0] = tmp[7..0].byte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getArray(index: Int, size: Int): ByteArray = copyOfRange(index, index + size)
fun ByteArray.putArray(index: Int, data: ByteArray) = System.arraycopy(data, 0, this, index, data.size)

fun ByteArray.getString(index: Int, size: Int, charset: Charset = Charsets.US_ASCII): String =
    getArray(index, size).toString(charset)

fun ByteArray.putString(index: Int, string: String, charset: Charset = Charsets.US_ASCII) =
    putArray(index, string.toByteArray(charset))

fun Byte.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
    ByteArray(size).also { it.putInt(0, this.asULong, size, order) }

fun Short.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
    ByteArray(size).also { it.putInt(0, this.asULong, size, order) }

fun Int.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
    ByteArray(size).also { it.putInt(0, this.asULong, size, order) }

fun Long.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
    ByteArray(size).also { it.putInt(0, this, size, order) }