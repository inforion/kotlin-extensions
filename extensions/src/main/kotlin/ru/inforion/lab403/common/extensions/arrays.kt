@file:Suppress("NOTHING_TO_INLINE", "unused", "DuplicatedCode")

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
inline val ByteArray.string get() = toString(Charsets.ISO_8859_1)

/**
 * Safely convert string to byte array without loss any information (even negative bytes)
 *
 * Безопасно конвертирует строку в массив байт с возможностью обратного преобразования
 */
inline val String.bytes get() = toByteArray(Charsets.ISO_8859_1)

inline fun ByteArray.sumOf(selector: (Byte) -> Byte) = fold(0) { sum, element -> sum + selector(element) }.byte

fun ByteArray.fromPDP11(start: Int = 0, end: Int = 0): ByteArray {
    val result = this.slice(start until end).toByteArray()
    for (k in result.indices step 2) {
        val tmp = result[k]
        result[k] = result[k + 1]
        result[k + 1] = tmp
    }
    return result
}

inline fun <T> Array<T>.bisectLeft(key: T): Int where T : Comparable<T> {
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

inline operator fun ByteArray.get(range: IntRange): ByteArray {
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

@PublishedApi
internal inline fun ByteArray.lastOrNullBy(data: ByteArray, size: Int = -1): Int? {
    require(size <= data.size) { "size must be <= data.size" }
    when {
        size != -1 -> if (size > this.size) return null
        else -> if (data.size > this.size) return null
    }
    return (if (size != -1) size else data.size) - 1
}

inline fun ByteArray.startswith(data: ByteArray, size: Int = -1): Boolean {
    val last = lastOrNullBy(data, size) ?: return false
    for (k in 0..last) {
        if (this[k] != data[k])
            return false
    }
    return true
}

fun ByteArray.fuzzyStartswith(data: ByteArray, size: Int = -1, fuzzy: List<Boolean>): Boolean {
    val last = lastOrNullBy(data, size) ?: return false
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

inline fun ByteArray.chunks(length: Int): List<ByteArray> {
    var offset = 0
    val dis = DataInputStream(inputStream())
    return List(this.size / length + 1) {
        val size = dis.available().coerceAtMost(length)
        val buffer = ByteArray(size)
        offset += dis.read(buffer, 0, size)
        buffer
    }
}

inline operator fun <T> List<T>.get(index: UInt) = get(index.int)
inline operator fun <T> Array<T>.get(index: UInt) = get(index.int)
inline operator fun ByteArray.get(index: UInt) = get(index.int)

inline val ByteBuffer.byte: Byte get() = get()
inline fun ByteBuffer.byte(value: Byte): ByteBuffer = put(value)
inline fun ByteBuffer.short(value: Short): ByteBuffer = putShort(value)
inline fun ByteBuffer.int(value: Int): ByteBuffer = putInt(value)
inline fun ByteBuffer.long(value: Long): ByteBuffer = putLong(value)

inline val ByteBuffer.ulong get() = long.ulong
inline val ByteBuffer.uint get() = int.uint
inline val ByteBuffer.ushort get() = short.ushort
inline val ByteBuffer.ubyte get() = byte.ubyte

inline fun byteBuffer(size: Int, directed: Boolean, order: ByteOrder = LITTLE_ENDIAN): ByteBuffer {
    val result = if (directed) ByteBuffer.allocateDirect(size) else ByteBuffer.allocate(size)
    return result.order(order)
}

inline operator fun <T, E : Enum<E>> Array<T>.get(e: Enum<E>): T = this[e.ordinal]

inline operator fun <T, E : Enum<E>> Array<T>.set(e: Enum<E>, value: T) {
    this[e.ordinal] = value
}

inline fun ByteArray.putUInt64(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt56(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt48(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt40(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt32(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt24(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt16(index: Int, value: ULong, order: ByteOrder = LITTLE_ENDIAN) = when (order) {
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

inline fun ByteArray.putUInt8(index: Int, value: ULong) {
    this[index + 0] = value[7..0].byte
}

inline fun ByteArray.putUInt(index: Int, value: ULong, size: Int, order: ByteOrder = LITTLE_ENDIAN) = when (size) {
    1 -> putUInt8(index, value)
    2 -> putUInt16(index, value, order)
    3 -> putUInt24(index, value, order)
    4 -> putUInt32(index, value, order)
    5 -> putUInt40(index, value, order)
    6 -> putUInt48(index, value, order)
    7 -> putUInt56(index, value, order)
    8 -> putUInt64(index, value, order)
    else -> throw IllegalArgumentException("Wrong int size!")
}

inline fun ByteArray.putInt64(index: Int, value: Long, order: ByteOrder = LITTLE_ENDIAN) = putUInt64(index, value.ulong, order)
inline fun ByteArray.putInt56(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) = putUInt56(index, value.ulong_z, order)
inline fun ByteArray.putInt48(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) = putUInt48(index, value.ulong_z, order)
inline fun ByteArray.putInt40(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) = putUInt40(index, value.ulong_z, order)
inline fun ByteArray.putInt32(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) = putUInt32(index, value.ulong_s, order)
inline fun ByteArray.putInt24(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) = putUInt24(index, value.ulong_s, order)
inline fun ByteArray.putInt16(index: Int, value: Int, order: ByteOrder = LITTLE_ENDIAN) = putUInt16(index, value.ulong_s, order)
inline fun ByteArray.putInt8(index: Int, value: Int) = putUInt8(index, value.ulong_s)

inline fun ByteArray.putInt(index: Int, value: Long, size: Int, order: ByteOrder = LITTLE_ENDIAN) =
    putUInt(index, value.ulong, size, order)


inline fun ByteArray.getUInt64(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 7].ulong_s, 7..0)
        .insert(this[index + 6].ulong_s, 15..8)
        .insert(this[index + 5].ulong_s, 23..16)
        .insert(this[index + 4].ulong_s, 31..24)
        .insert(this[index + 3].ulong_s, 39..32)
        .insert(this[index + 2].ulong_s, 47..40)
        .insert(this[index + 1].ulong_s, 55..48)
        .insert(this[index + 0].ulong_s, 63..56)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
        .insert(this[index + 4].ulong_s, 39..32)
        .insert(this[index + 5].ulong_s, 47..40)
        .insert(this[index + 6].ulong_s, 55..48)
        .insert(this[index + 7].ulong_s, 63..56)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt56(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 6].ulong_s, 7..0)
        .insert(this[index + 5].ulong_s, 15..8)
        .insert(this[index + 4].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
        .insert(this[index + 2].ulong_s, 39..32)
        .insert(this[index + 1].ulong_s, 47..40)
        .insert(this[index + 0].ulong_s, 55..48)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
        .insert(this[index + 4].ulong_s, 39..32)
        .insert(this[index + 5].ulong_s, 47..40)
        .insert(this[index + 6].ulong_s, 55..48)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt48(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 5].ulong_s, 7..0)
        .insert(this[index + 4].ulong_s, 15..8)
        .insert(this[index + 3].ulong_s, 23..16)
        .insert(this[index + 2].ulong_s, 31..24)
        .insert(this[index + 1].ulong_s, 39..32)
        .insert(this[index + 0].ulong_s, 47..40)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
        .insert(this[index + 4].ulong_s, 39..32)
        .insert(this[index + 5].ulong_s, 47..40)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt40(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 4].ulong_s, 7..0)
        .insert(this[index + 3].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 1].ulong_s, 31..24)
        .insert(this[index + 0].ulong_s, 39..32)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
        .insert(this[index + 4].ulong_s, 39..32)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt32(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 3].ulong_s, 7..0)
        .insert(this[index + 2].ulong_s, 15..8)
        .insert(this[index + 1].ulong_s, 23..16)
        .insert(this[index + 0].ulong_s, 31..24)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt24(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 2].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 0].ulong_s, 23..16)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt16(index: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (order) {
    BIG_ENDIAN -> insert(this[index + 1].ulong_s, 7..0)
        .insert(this[index + 0].ulong_s, 15..8)
    LITTLE_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.getUInt8(index: Int) = this[index].ulong_s mask 8

inline fun ByteArray.getUInt(index: Int, size: Int, order: ByteOrder = LITTLE_ENDIAN): ULong = when (size) {
    1 -> getUInt8(index) mask 8
    2 -> getUInt16(index, order)
    3 -> getUInt24(index, order)
    4 -> getUInt32(index, order)
    5 -> getUInt40(index, order)
    6 -> getUInt48(index, order)
    7 -> getUInt56(index, order)
    8 -> getUInt64(index, order)
    else -> throw IllegalArgumentException("Wrong int size!")
}

inline fun ByteArray.getInt64(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt64(index, order).long
inline fun ByteArray.getInt56(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt56(index, order).long
inline fun ByteArray.getInt48(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt48(index, order).long
inline fun ByteArray.getInt40(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt40(index, order).long
inline fun ByteArray.getInt32(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt32(index, order).int
inline fun ByteArray.getInt24(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt24(index, order).int
inline fun ByteArray.getInt16(index: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt16(index, order).int
inline fun ByteArray.getInt8(index: Int) = getUInt8(index).int

inline fun ByteArray.getInt(index: Int, size: Int, order: ByteOrder = LITTLE_ENDIAN) = getUInt(index, size, order).long

inline fun ByteArray.getDouble(index: Int, order: ByteOrder = LITTLE_ENDIAN): Double = when (order) {
    LITTLE_ENDIAN -> insert(this[index + 7].ulong_s, 7..0)
        .insert(this[index + 6].ulong_s, 15..8)
        .insert(this[index + 5].ulong_s, 23..16)
        .insert(this[index + 4].ulong_s, 31..24)
        .insert(this[index + 3].ulong_s, 39..32)
        .insert(this[index + 2].ulong_s, 47..40)
        .insert(this[index + 1].ulong_s, 55..48)
        .insert(this[index + 0].ulong_s, 63..56)
        .ieee754()
    BIG_ENDIAN -> insert(this[index + 0].ulong_s, 7..0)
        .insert(this[index + 1].ulong_s, 15..8)
        .insert(this[index + 2].ulong_s, 23..16)
        .insert(this[index + 3].ulong_s, 31..24)
        .insert(this[index + 4].ulong_s, 39..32)
        .insert(this[index + 5].ulong_s, 47..40)
        .insert(this[index + 6].ulong_s, 55..48)
        .insert(this[index + 7].ulong_s, 63..56)
        .ieee754()
    else -> throw IllegalArgumentException("WRONG BYTE ORDER")
}

inline fun ByteArray.putDouble(index: Int, value: Double, order: ByteOrder = LITTLE_ENDIAN) =
    putUInt64(index, value.ieee754AsUnsigned(), order)

inline fun ByteArray.getArray(index: Int, size: Int): ByteArray = copyOfRange(index, index + size)

inline fun ByteArray.putArray(index: Int, data: ByteArray) = System.arraycopy(data, 0, this, index, data.size)

inline fun ByteArray.getString(index: Int, size: Int, charset: Charset = Charsets.US_ASCII): String =
    getArray(index, size).toString(charset)

inline fun ByteArray.putString(index: Int, string: String, charset: Charset = Charsets.US_ASCII) =
    putArray(index, string.toByteArray(charset))

inline fun Byte.pack(size: Int) = ulong_z.pack(size)

inline fun Short.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN) = ulong_z.pack(size, order)

inline fun Int.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN) = ulong_z.pack(size, order)

inline fun Long.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN) = ulong.pack(size, order)

inline fun UByte.pack(size: Int) = ulong_z.pack(size)

inline fun UShort.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN) = ulong_z.pack(size, order)

inline fun UInt.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN) = ulong_z.pack(size, order)

inline fun ULong.pack(size: Int, order: ByteOrder = LITTLE_ENDIAN): ByteArray =
    ByteArray(size).also { it.putUInt(0, this, size, order) }

inline fun<T> Array<T>.reassign(ctor: (T) -> T) = forEachIndexed { index: Int, value: T ->
    this[index] = ctor(value)
}

inline fun<T> Array<T>.reassignIndexed(ctor: (Int, T) -> T) = forEachIndexed { index: Int, value: T ->
    this[index] = ctor(index, value)
}