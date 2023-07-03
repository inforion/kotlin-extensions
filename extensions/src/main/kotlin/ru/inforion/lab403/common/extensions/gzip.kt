@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 28/06/16.
 */

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Wraps [this] output stream in [GZIPOutputStream] if data compression is enabled otherwise returns [this]
 *
 * @param enabled if true wraps otherwise return original stream
 */
fun OutputStream.toGZIPOutputStream(enabled: Boolean = true) = if (enabled) GZIPOutputStream(this) else this

/**
 * Wraps [this] input stream in [GZIPInputStream] if data compression is enabled otherwise returns [this]
 *
 * @param enabled if true wraps otherwise return original stream
 */
fun InputStream.toGZIPInputStream(enabled: Boolean = true) = if (enabled) GZIPInputStream(this) else this

fun ByteArray.gzip(): ByteArray {
    val stream = ByteArrayOutputStream(size)
    stream.toGZIPOutputStream().use { it.write(this) }
    return stream.toByteArray()
}

fun ByteArray.ungzip(): ByteArray = ByteArrayInputStream(this)
    .toGZIPInputStream().readAllBytes()