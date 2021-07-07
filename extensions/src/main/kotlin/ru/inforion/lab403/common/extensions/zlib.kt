package ru.inforion.lab403.common.extensions

/**
 * Created by Alexei Gladkikh on 28/06/16.
 */

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

object zlib {
    fun compress(data: ByteArray): ByteArray {
        val deflater = Deflater()
        deflater.setInput(data)

        val outputStream = ByteArrayOutputStream(data.size)

        deflater.finish()
        val buffer = ByteArray(1024)
        while (!deflater.finished()) {
            val count = deflater.deflate(buffer) // returns the generated code... index
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        val output = outputStream.toByteArray()

        deflater.end()

        return output
    }

    fun decompress(data: ByteArray): ByteArray {
        val inflater = Inflater()
        inflater.setInput(data)

        val outputStream = ByteArrayOutputStream(data.size)
        val buffer = ByteArray(1024)
        while (!inflater.finished()) {
            val count = inflater.inflate(buffer)
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        val output = outputStream.toByteArray()

        inflater.end()

        return output
    }
}
