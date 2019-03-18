package ru.inforion.lab403.common.extensions

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by Alexei Gladkikh on 10/09/16.
 */
fun Serializable.serialize(): ByteArray {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeUnshared(this)
    return baos.toByteArray()
}

fun deserialize(data: ByteArray): Serializable {
    return try {
        val ois = ObjectInputStream(data.inputStream())
        ois.readUnshared() as Serializable
    } catch (e: Exception) {
        println(data.hexlify())
        throw e
    }
}