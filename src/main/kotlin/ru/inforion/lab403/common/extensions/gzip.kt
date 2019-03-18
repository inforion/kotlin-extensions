package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 28/06/16.
 */

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

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