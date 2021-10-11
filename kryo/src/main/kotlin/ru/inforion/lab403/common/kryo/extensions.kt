package ru.inforion.lab403.common.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import ru.inforion.lab403.common.extensions.cast
import java.io.*

val Input.stream get() = DataInputStream(this)

val Output.stream get() = DataOutputStream(this)

val ByteArray.input get() = Input(this)

val InputStream.input get() = Input(this)

fun <T> Kryo.writeClassAndObject(stream: OutputStream, obj: T) {
    val output = Output(stream)
    writeClassAndObject(output, obj)
    output.flush()
}

fun <T> Kryo.readClassAndObject(stream: InputStream): T {
    val input = Input(stream)
    val obj = readClassAndObject(input)
    return obj.cast()
}

fun <T> Kryo.readClassAndObject(bytes: ByteArray): T {
    val input = Input(bytes)
    val obj = readClassAndObject(input)
    return obj.cast()
}