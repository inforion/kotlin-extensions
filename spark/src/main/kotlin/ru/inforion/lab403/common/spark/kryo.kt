package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import ru.inforion.lab403.common.extensions.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.IllegalArgumentException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.system.exitProcess


// TODO: arrays, ranges

internal val Input.stream get() = DataInputStream(this)

internal val Output.stream get() = DataOutputStream(this)

object UByteSerializer : Serializer<UByte>() {
    override fun write(kryo: Kryo, output: Output, obj: UByte) = output.writeByte(obj.int_s)
    override fun read(kryo: Kryo, input: Input, type: Class<UByte>) = input.readByte().ubyte
}

object UShortSerializer : Serializer<UShort>() {
    override fun write(kryo: Kryo, output: Output, obj: UShort) = output.writeShort(obj.int_s)
    override fun read(kryo: Kryo, input: Input, type: Class<UShort>) = input.readShort().ushort
}

object UIntSerializer : Serializer<UInt>() {
    override fun write(kryo: Kryo, output: Output, obj: UInt) = output.writeInt(obj.int)
    override fun read(kryo: Kryo, input: Input, type: Class<UInt>) = input.readInt().uint
}

object ULongSerializer : Serializer<ULong>() {
    override fun write(kryo: Kryo, output: Output, obj: ULong) = output.writeLong(obj.long)
    override fun read(kryo: Kryo, input: Input, type: Class<ULong>) = input.readLong().ulong
}

object IntRangeSerializer : Serializer<IntRange>() {
    override fun write(kryo: Kryo, output: Output, obj: IntRange) = output.stream.writeIntRange(obj)
    override fun read(kryo: Kryo, input: Input, type: Class<IntRange>) = input.stream.readIntRange()
}

object LongRangeSerializer : Serializer<LongRange>() {
    override fun write(kryo: Kryo, output: Output, obj: LongRange) = output.stream.writeLongRange(obj)
    override fun read(kryo: Kryo, input: Input, type: Class<LongRange>) = input.stream.readLongRange()
}

object ByteBufferSerializer : Serializer<ByteBuffer>() {
    override fun write(kryo: Kryo, output: Output, obj: ByteBuffer) = output.stream.writeByteBuffer(obj)
    override fun read(kryo: Kryo, input: Input, type: Class<ByteBuffer>) = input.stream.readByteBuffer()
}

fun Kryo.registerUnsigned() {
    register(UByte::class.java, UByteSerializer)
    register(UShort::class.java, UShortSerializer)
    register(UInt::class.java, UIntSerializer)
    register(ULong::class.java, ULongSerializer)
}

fun Kryo.registerRanges() {
    register(IntRange::class.java, IntRangeSerializer)
    register(LongRange::class.java, LongRangeSerializer)
}

fun Kryo.registerByteBuffer() {
    register(ByteBuffer::class.java, ByteBufferSerializer)
}