package ru.inforion.lab403.common.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import ru.inforion.lab403.common.extensions.cast
import java.io.*

inline val Input.stream get() = DataInputStream(this)

inline val Output.stream get() = DataOutputStream(this)

inline val ByteArray.input get() = Input(this)

inline val InputStream.input get() = Input(this)

inline val OutputStream.output get() = Output(this)

fun <T> Kryo.writeClassAndObject(stream: OutputStream, obj: T)= stream.output.apply {
    writeClassAndObject(this, obj)
    flush()
}

fun <T> Kryo.readClassAndObject(stream: InputStream): T = readClassAndObject(stream.input).cast()

fun <T> Kryo.readClassAndObject(bytes: ByteArray): T = readClassAndObject(bytes.input).cast()