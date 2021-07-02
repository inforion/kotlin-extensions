@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 28/06/16.
 */

import java.io.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

@Deprecated("Use gzip and ungzip extensions method instead")
object gzip {
    fun compress(data: ByteArray): ByteArray {
        val stream = ByteArrayOutputStream(data.size)
        val gzip = GZIPOutputStream(stream)
        gzip.write(data)
        return stream.toByteArray()
    }

    fun decompress(path: String): ByteArray {
        val gzip = GZIPInputStream(FileInputStream(path))
        val stream = ByteArrayOutputStream()
        gzip.copyTo(stream)
        return stream.toByteArray()
    }

    fun decompress(compressed: ByteArray): ByteArray {
        val gzip = GZIPInputStream(ByteArrayInputStream(compressed))
        val stream = ByteArrayOutputStream()
        gzip.copyTo(stream)
        return stream.toByteArray()
    }
}


inline fun OutputStream.toGZIPOutputStream() = GZIPOutputStream(this)

inline fun InputStream.toGZIPInputStream() = GZIPInputStream(this)

inline fun ByteArray.gzip(): ByteArray {
    val stream = ByteArrayOutputStream(size)
    stream.toGZIPOutputStream().use { it.write(this) }
    return stream.toByteArray()
}

inline fun ByteArray.ungzip(): ByteArray = ByteArrayInputStream(this)
    .toGZIPInputStream().readAllBytes()