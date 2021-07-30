package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import ru.inforion.lab403.common.extensions.*


// TODO: arrays, ranges

class UByteSerializer : Serializer<UByte>() {
    override fun write(kryo: Kryo, output: Output, obj: UByte) = output.writeByte(obj.int_s)
    override fun read(kryo: Kryo, input: Input, type: Class<UByte>) = input.readByte().ubyte
}

class UShortSerializer : Serializer<UShort>() {
    override fun write(kryo: Kryo, output: Output, obj: UShort) = output.writeShort(obj.int_s)
    override fun read(kryo: Kryo, input: Input, type: Class<UShort>) = input.readShort().ushort
}

class UIntSerializer : Serializer<UInt>() {
    override fun write(kryo: Kryo, output: Output, obj: UInt) = output.writeInt(obj.int)
    override fun read(kryo: Kryo, input: Input, type: Class<UInt>) = input.readInt().uint
}

class ULongSerializer : Serializer<ULong>() {
    override fun write(kryo: Kryo, output: Output, obj: ULong) = output.writeLong(obj.long)
    override fun read(kryo: Kryo, input: Input, type: Class<ULong>) = input.readLong().ulong
}

fun Kryo.registerUnsigned() {
    register(UByte::class.java, UByteSerializer())
    register(UShort::class.java, UShortSerializer())
    register(UInt::class.java, UIntSerializer())
    register(ULong::class.java, ULongSerializer())
}
