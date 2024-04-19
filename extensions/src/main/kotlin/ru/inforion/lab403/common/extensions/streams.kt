@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder.BIG_ENDIAN
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.collections.ArrayList

val emptyInputStream = ByteArray(0).inputStream()


fun InputStream.readAvailableBytes(count: Int = -1): ByteArray {
    val toRead = if (count > 0) minOf(available(), count) else available()
    return readNBytes(toRead)
}


fun InputStream.readWhile(
    leftover: Boolean = true,
    capacity: Int = 32,
    predicate: (Int) -> Boolean
): ByteArray {
    val result = ByteArrayOutputStream(capacity)
    while (true) {
        val value = read()

        if (value == -1)
            throw EOFException()

        if (!predicate(value)) {
            if (leftover) result.write(value)
            return result.toByteArray()
        }

        result.write(value)
    }
}


fun InputStream.readWhileOrNull(
    leftover: Boolean = true,
    capacity: Int = 32,
    predicate: (Int) -> Boolean
) = try {
    readWhile(leftover, capacity, predicate)
} catch (error: EOFException) {
    null
}


const val DEFAULT_BYTE_BUFFER_CHUNK = 0x80_0000
const val DEFAULT_BYTE_BUFFER_MARKER = 0x6EADBEEF

fun InputStream.readBufferData(
    dst: ByteBuffer,
    offset: Int = 0,
    chunk: Int = DEFAULT_BYTE_BUFFER_CHUNK
): Int {
    var total = 0
    val buf = ByteArray(chunk)
    dst.position(offset)
    do {
        val count = read(buf, 0, chunk)
        if (count > 0) {
            dst.put(buf, 0, count)
            total += count
        } else break
    } while (count > 0)
    return total
}

fun OutputStream.writeBufferData(
    src: ByteBuffer,
    offset: Int = 0,
    chunk: Int = DEFAULT_BYTE_BUFFER_CHUNK
) {
    src.position(offset)
    val buffer = ByteArray(chunk)
    do {
        val count = chunk.coerceAtMost(src.remaining())
        src.get(buffer, 0, count)
        write(buffer, 0, count)
    } while (src.remaining() != 0)
}

fun DataInputStream.readByteBuffer(
    chunk: Int = DEFAULT_BYTE_BUFFER_CHUNK,
    marker: Int = DEFAULT_BYTE_BUFFER_MARKER
): ByteBuffer {
    val isLittle = readBoolean()
    val isDirect = readBoolean()
    val limit = readInt()
    val position = readInt()

    val obj = byteBuffer(limit, isDirect, if (isLittle) LITTLE_ENDIAN else BIG_ENDIAN)

    val array = ByteArray(chunk)
    while (obj.remaining() != 0) {
        val remain = obj.remaining()
        val size = if (remain > array.size) array.size else remain
        val count = read(array, 0, size)
        obj.put(array, 0, count)
    }

    check(readInt() == marker) { "Serialization marker != $marker" }

    return obj.position(position)
}

fun DataOutputStream.writeByteBuffer(
    obj: ByteBuffer,
    chunk: Int = DEFAULT_BYTE_BUFFER_CHUNK,
    marker: Int = DEFAULT_BYTE_BUFFER_MARKER
) {
    writeBoolean(obj.order() == LITTLE_ENDIAN)
    writeBoolean(obj.isDirect)
    writeInt(obj.limit())
    writeInt(obj.position())
    val oldPosition = obj.position()
    obj.position(0)
    val array = ByteArray(chunk)
    while (obj.remaining() != 0) {
        val size = minOf(array.size, obj.remaining())
        obj.get(array, 0, size)
        write(array, 0, size)
    }
    writeInt(marker)
    obj.position(oldPosition)
}


fun DataOutputStream.writeLongRange(range: LongRange) {
    writeLong(range.first)
    writeLong(range.last)
}

fun DataInputStream.readLongRange() = LongRange(readLong(), readLong())


fun DataOutputStream.writeIntRange(range: IntRange) {
    writeInt(range.first)
    writeInt(range.last)
}

fun DataInputStream.readIntRange() = IntRange(readInt(), readInt())


fun DataOutputStream.writeTribyte(v: Int) {
    write(v ushr 16 and 0xFF)
    write(v ushr 8 and 0xFF)
    write(v ushr 0 and 0xFF)
}

fun DataInputStream.readTribyte(): Int {
    val ch1 = read()
    val ch2 = read()
    val ch3 = read()
    if (ch1 or ch2 or ch3 < 0) throw EOFException()
    return (ch1 shl 16) or (ch2 shl 8) or (ch3 shl 0)
}

inline fun DataOutputStream.writeDate(timestamp: Date) = writeLong(timestamp.time)

inline fun DataOutputStream.writeULong(value: ULong) = writeLong(value.long)

inline fun DataInputStream.readDate(): Date = Date(readLong())

inline fun DataInputStream.readULong() = readLong().ulong

fun DataOutputStream.writeLongOptional(value: Long?) {
    writeBoolean(value != null)
    if (value != null) writeLong(value)
}

fun DataOutputStream.writeULongOptional(value: ULong?) {
    writeBoolean(value != null)
    if (value != null) writeLong(value.long)
}

fun DataInputStream.readLongOrNull() = if (readBoolean()) readULong() else null

fun DataInputStream.readULongOrNull() = if (readBoolean()) readULong() else null

fun DataOutputStream.writeIntOptional(value: Int?) {
    writeBoolean(value != null)
    if (value != null) writeInt(value)
}

fun DataInputStream.readIntOrNull() = if (readBoolean()) readInt() else null

fun DataOutputStream.writeStringIso(value: String) {
    writeInt(value.length)
    write(value.bytes)
}

fun DataInputStream.readStringIso() = readNBytes(readInt()).string

fun DataOutputStream.writeStringIsoOptional(value: String?) {
    writeBoolean(value != null)
    if (value != null) writeStringIso(value)
}

fun DataInputStream.readStringIsoOrNull() = if (readBoolean()) readStringIso() else null

fun DataOutputStream.writeByteArray(value: ByteArray) {
    writeInt(value.size)
    write(value)
}

fun DataInputStream.readByteArray(): ByteArray = readNBytes(readInt())

fun DataOutputStream.writeByteArrayOptional(value: ByteArray?) {
    writeBoolean(value != null)
    if (value != null) writeByteArray(value)
}

fun DataInputStream.readByteArrayOrNull(): ByteArray? = if (readBoolean()) readByteArray() else null

fun DataOutputStream.writeULongArray(value: ULongArray) {
    writeInt(value.size)
    value.forEach { writeULong(it) }
}

fun DataInputStream.readULongArray() = ULongArray(readInt()) { readULong() }

fun <K, V> DataOutputStream.writeDictionary(
    value: Dictionary<K, V>,
    write: DataOutputStream.(Map.Entry<K, V>) -> Unit
) {
    writeInt(value.size)
    value.forEach { write(it) }
}

fun <K, V> DataInputStream.readDictionary(
    read: DataInputStream.() -> Pair<K, V>
): Dictionary<K, V> {
    val size = readInt()
    val result = dictionary<K, V>(capacity(size))
    repeat(readInt()) {
        val (key, value) = read(this)
        result[key] = value
    }
    return result
}

fun <K, V> DataInputStream.readDictionary(
    key: DataInputStream.() -> K,
    value: DataInputStream.() -> V,
): Dictionary<K, V> {
    val size = readInt()
    val result = dictionary<K, V>(capacity(size))
    repeat(size) { result[key()] = value() }
    return result
}

fun <T> DataOutputStream.writeList(value: List<T>, write: DataOutputStream.(T) -> Unit) {
    writeInt(value.size)
    value.forEach { write(it) }
}

fun <T> DataInputStream.readList(read: DataInputStream.() -> T): List<T> {
    val size = readInt()
    val result = ArrayList<T>(size)
    repeat(size) { result.add(read(this)) }
    return result
}

inline fun <T : Enum<T>> DataOutputStream.writeEnumValue(value: T) = writeStringIso(value.name)

inline fun <reified T : Enum<T>> DataInputStream.readEnumValue() = enumValueOf<T>(readStringIso())

inline fun OutputStream.toZipOutputStream() = if (this is ZipOutputStream) this else ZipOutputStream(this)

inline fun InputStream.toZipInputStream() = if (this is ZipInputStream) this else ZipInputStream(this)
