@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.DataInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ByteOrder.*
import java.nio.charset.Charset

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
    for (k in 0 until result.size step 2) {
        val tmp = result[k]
        result[k] = result[k + 1]
        result[k + 1] = tmp
    }
    return result
}

fun <T> Array<T>.bisectLeft(key: T): Int where T : Comparable<T> {
    var idx = Math.min(this.size - 1, Math.abs(this.sorted().binarySearch(key)))
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
    if (size > data.size) {
        throw IllegalArgumentException("size must be <= data.size")
    }
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
    if (size > data.size) {
        throw IllegalArgumentException("size must be <= data.size")
    }
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

inline fun<T>ByteArray.split(byte: Byte, limit: Int = 0, modify: (ByteArray) -> T): List<T> {
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

inline fun<T>ByteArray.split(char: Char, limit: Int = 0, modify: (ByteArray) -> T) = split(char.toByte(), limit, modify)

inline fun ByteArray.split(char: Char, limit: Int = 0) = split(char.toByte(), limit) { it }

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

operator fun <T, E: Enum<E>>Array<T>.get(e: Enum<E>): T = this[e.ordinal]
operator fun <T, E: Enum<E>>Array<T>.set(e: Enum<E>, value: T) {
    this[e.ordinal] = value
}

fun ByteArray.putInt64(index: Int, value: Long, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[63..56].asByte
            this[index + 1] = value[55..48].asByte
            this[index + 2] = value[47..40].asByte
            this[index + 3] = value[39..32].asByte
            this[index + 4] = value[31..24].asByte
            this[index + 5] = value[23..16].asByte
            this[index + 6] = value[15..8].asByte
            this[index + 7] = value[7..0].asByte
        }
        LITTLE_ENDIAN -> {
            this[index + 7] = value[63..56].asByte
            this[index + 6] = value[55..48].asByte
            this[index + 5] = value[47..40].asByte
            this[index + 4] = value[39..32].asByte
            this[index + 3] = value[31..24].asByte
            this[index + 2] = value[23..16].asByte
            this[index + 1] = value[15..8].asByte
            this[index + 0] = value[7..0].asByte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt32(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[31..24].asByte
            this[index + 1] = value[23..16].asByte
            this[index + 2] = value[15..8].asByte
            this[index + 3] = value[7..0].asByte
        }
        LITTLE_ENDIAN -> {
            this[index + 3] = value[31..24].asByte
            this[index + 2] = value[23..16].asByte
            this[index + 1] = value[15..8].asByte
            this[index + 0] = value[7..0].asByte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt24(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[23..16].asByte
            this[index + 1] = value[15..8].asByte
            this[index + 2] = value[7..0].asByte
        }
        LITTLE_ENDIAN -> {
            this[index + 2] = value[23..16].asByte
            this[index + 1] = value[15..8].asByte
            this[index + 0] = value[7..0].asByte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt16(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) {
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = value[15..8].asByte
            this[index + 1] = value[7..0].asByte
        }
        LITTLE_ENDIAN -> {
            this[index + 1] = value[15..8].asByte
            this[index + 0] = value[7..0].asByte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.putInt8(index: Int, value: Int) {
    this[index + 0] = value[7..0].asByte
}

fun ByteArray.putInt(index: Int, value: Long, size: Int, order: ByteOrder) {
    when (size) {
        1 -> putInt8(index, value.asInt)
        2 -> putInt16(index, value.asInt, order)
        3 -> putInt24(index, value.asInt, order)
        4 -> putInt32(index, value.asInt, order)
        8 -> putInt64(index, value, order)
        else -> throw IllegalArgumentException("Wrong int size!")
    }
}

fun ByteArray.getInt64(index: Int, order: ByteOrder = LITTLE_ENDIAN): Long {
    return when (order) {
        BIG_ENDIAN -> {
            insert(this[index + 7].asULong, 7..0)
                    .insert(this[index + 6].asULong, 15..8)
                    .insert(this[index + 5].asULong, 23..16)
                    .insert(this[index + 4].asULong, 31..24)
                    .insert(this[index + 3].asULong, 39..32)
                    .insert(this[index + 2].asULong, 47..40)
                    .insert(this[index + 1].asULong, 55..48)
                    .insert(this[index + 0].asULong, 63..56)
        }
        LITTLE_ENDIAN -> {
            insert(this[index + 0].asULong, 7..0)
                    .insert(this[index + 1].asULong, 15..8)
                    .insert(this[index + 2].asULong, 23..16)
                    .insert(this[index + 3].asULong, 31..24)
                    .insert(this[index + 4].asULong, 39..32)
                    .insert(this[index + 5].asULong, 47..40)
                    .insert(this[index + 6].asULong, 55..48)
                    .insert(this[index + 7].asULong, 63..56)
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getInt32(index: Int, order: ByteOrder = LITTLE_ENDIAN): Long {
    return when (order) {
        BIG_ENDIAN -> {
            insert(this[index + 3].asULong, 7..0)
                    .insert(this[index + 2].asULong, 15..8)
                    .insert(this[index + 1].asULong, 23..16)
                    .insert(this[index + 0].asULong, 31..24)
        }
        LITTLE_ENDIAN -> {
            insert(this[index + 0].asULong, 7..0)
                    .insert(this[index + 1].asULong, 15..8)
                    .insert(this[index + 2].asULong, 23..16)
                    .insert(this[index + 3].asULong, 31..24)
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getInt24(index: Int, order: ByteOrder = LITTLE_ENDIAN): Long {
    return when (order) {
        BIG_ENDIAN -> {
            insert(this[index + 2].asULong, 7..0)
                    .insert(this[index + 1].asULong, 15..8)
                    .insert(this[index + 0].asULong, 23..16)
        }
        LITTLE_ENDIAN -> {
            insert(this[index + 0].asULong, 7..0)
                    .insert(this[index + 1].asULong, 15..8)
                    .insert(this[index + 2].asULong, 23..16)
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getInt16(index: Int, order: ByteOrder = LITTLE_ENDIAN): Long {
    return when (order) {
        BIG_ENDIAN -> {
            insert(this[index + 1].asULong, 7..0)
                    .insert(this[index + 0].asULong, 15..8)
        }
        LITTLE_ENDIAN -> {
            insert(this[index + 0].asULong, 7..0)
                    .insert(this[index + 1].asULong, 15..8)
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getInt8(index: Int): Int {
    return this[index].asUInt
}

fun ByteArray.getValue(index: Int, bytesNumber: Int, order: ByteOrder = LITTLE_ENDIAN): Long {
    return when(bytesNumber){
        1 -> getInt8(index).toULong()
        2 -> getInt16(index, order)
        3 -> getInt24(index, order)
        4 -> getInt32(index, order)
        8 -> getInt64(index, order)
        else -> throw IllegalArgumentException("Available bytesNumber is 1, 2, 3, 4, 8")
    }
}

fun ByteArray.getDouble(index: Int, order: ByteOrder = LITTLE_ENDIAN): Double {
    return when (order) {
        LITTLE_ENDIAN -> {
            val tmp = insert(this[index + 7].asULong, 7..0)
                    .insert(this[index + 6].asULong, 15..8)
                    .insert(this[index + 5].asULong, 23..16)
                    .insert(this[index + 4].asULong, 31..24)
                    .insert(this[index + 3].asULong, 39..32)
                    .insert(this[index + 2].asULong, 47..40)
                    .insert(this[index + 1].asULong, 55..48)
                    .insert(this[index + 0].asULong, 63..56)
            tmp.ieee754()
        }
        BIG_ENDIAN -> {
            val tmp = insert(this[index + 0].asULong, 7..0)
                    .insert(this[index + 1].asULong, 15..8)
                    .insert(this[index + 2].asULong, 23..16)
                    .insert(this[index + 3].asULong, 31..24)
                    .insert(this[index + 4].asULong, 39..32)
                    .insert(this[index + 5].asULong, 47..40)
                    .insert(this[index + 6].asULong, 55..48)
                    .insert(this[index + 7].asULong, 63..56)
            tmp.ieee754()
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getInt(index: Int, size: Int, order: ByteOrder = LITTLE_ENDIAN): Long {
    return when (size) {
        1 -> getInt8(index).asULong
        2 -> getInt16(index, order)
        3 -> getInt24(index, order)
        4 -> getInt32(index, order)
        8 -> getInt64(index, order)
        else -> throw IllegalArgumentException("Wrong int size!")
    }
}

fun ByteArray.putDouble(index: Int, value: Double, order: ByteOrder = LITTLE_ENDIAN) {
    val tmp = value.ieee754()
    when (order) {
        BIG_ENDIAN -> {
            this[index + 0] = tmp[63..56].asByte
            this[index + 1] = tmp[55..48].asByte
            this[index + 2] = tmp[47..40].asByte
            this[index + 3] = tmp[39..32].asByte
            this[index + 4] = tmp[31..24].asByte
            this[index + 5] = tmp[23..16].asByte
            this[index + 6] = tmp[15..8].asByte
            this[index + 7] = tmp[7..0].asByte
        }
        LITTLE_ENDIAN -> {
            this[index + 7] = tmp[63..56].asByte
            this[index + 6] = tmp[55..48].asByte
            this[index + 5] = tmp[47..40].asByte
            this[index + 4] = tmp[39..32].asByte
            this[index + 3] = tmp[31..24].asByte
            this[index + 2] = tmp[23..16].asByte
            this[index + 1] = tmp[15..8].asByte
            this[index + 0] = tmp[7..0].asByte
        }
        else -> throw IllegalArgumentException("WRONG BYTE ORDER")
    }
}

fun ByteArray.getArray(index: Int, size: Int): ByteArray = copyOfRange(index, index + size)
fun ByteArray.putArray(index: Int, data: ByteArray) = System.arraycopy(data, 0, this, index, data.size)

fun ByteArray.getString(index: Int, size: Int, charset: Charset = Charsets.US_ASCII): String = getArray(index, size).toString(charset)
fun ByteArray.putString(index: Int, string: String, charset: Charset = Charsets.US_ASCII) = putArray(index, string.toByteArray(charset))

fun Byte.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
        ByteArray(size).also { it.putInt(0, this.asULong, size, order) }
fun Short.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
        ByteArray(size).also { it.putInt(0, this.asULong, size, order) }
fun Int.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
        ByteArray(size).also { it.putInt(0, this.asULong, size, order) }
fun Long.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
        ByteArray(size).also { it.putInt(0, this, size, order) }

inline fun ByteArray.sumByLong(selector: (Byte) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun ShortArray.sumByLong(selector: (Short) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun IntArray.sumByLong(selector: (Int) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun LongArray.sumByLong(selector: (Long) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun <T> Array<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}