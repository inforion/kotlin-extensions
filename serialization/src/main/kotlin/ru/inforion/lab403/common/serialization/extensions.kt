@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.serialization

import ru.inforion.lab403.common.extensions.toGZIPInputStream
import ru.inforion.lab403.common.extensions.toGZIPOutputStream
import java.io.*
import java.nio.ByteBuffer
import java.util.zip.GZIPOutputStream


/**
 * Serialize [this] object in specified [output] stream
 *
 * @param output is output stream to store serialized object
 * @param compress if true stream will be wrapped with GZIP
 *
 * WARNING: this method closes the input stream, so after using the method,
 *   writing to the stream is no longer allowed. This is due to the use of
 *   the GZIP stream compression feature. To write data correctly with a
 *   GZIP stream, call the [GZIPOutputStream.finish] method required,
 *   which can be called in the [OutputStream.close] method
 */
inline fun <T: Serializable> T.serialize(output: OutputStream, compress: Boolean) =
    output.toGZIPOutputStream(compress).use { ObjectOutputStream(it).writeUnshared(this) }

/**
 * Calculate the size of an object after serialization by dummy "writing" it to an array
 * In fact data is not written to memory, but only the pointer is shifted in the [DummyOutputStream]
 *
 * @param compress if true calculate size for compressed with GZIP
 */
inline fun <T: Serializable> T.calcObjectSize(compress: Boolean) =
    DummyOutputStream().apply { serialize(this, compress) }.written

/**
 * Serialize an object to a buffer
 *
 * @param compress if true serialized data will be GZIPed
 * @param directed required or not buffer to be directed
 */
inline fun <T: Serializable> T.serialize(compress: Boolean, directed: Boolean): ByteBuffer {
//    TODO: code for serialization verification, make it configurable
//    val stream1 = DummyOutputStream(verifiable = true).apply { serialize(this, compress) }
//    val stream2 = DummyOutputStream(stream1, verifiable = true).apply { serialize(this, compress) }
    val size = calcObjectSize(compress)
    return BufferOutputStream(size, directed).apply { serialize(this, compress) }.buffer
}

/**
 * Serialize an object to a array.
 *
 * @param compress if true serialized data will be GZIPed
 */
inline fun <T: Serializable> T.serialize(compress: Boolean = false): ByteArray = serialize(compress, false).array()

/**
 * Deserializes object from [this] stream
 */
@Suppress("UNCHECKED_CAST")
inline fun <T: Serializable, S: InputStream> S.deserialize(compress: Boolean = false) =
    ObjectInputStream(toGZIPInputStream(compress)).readUnshared() as T

/**
 * Deserializes object from [this] bytes
 */
inline fun <T: Serializable> ByteArray.deserialize(compress: Boolean = false): T = inputStream().deserialize(compress)
