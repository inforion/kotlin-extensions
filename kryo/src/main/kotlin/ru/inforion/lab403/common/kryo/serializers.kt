package ru.inforion.lab403.common.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.joda.time.DateTime
import ru.inforion.lab403.common.extensions.*
import ru.inforion.lab403.common.jodatime.readDateTime
import ru.inforion.lab403.common.jodatime.writeDateTime
import java.nio.ByteBuffer

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

object DateTimeSerializer : Serializer<DateTime>() {
    override fun write(kryo: Kryo, output: Output, obj: DateTime) = output.stream.writeDateTime(obj)
    override fun read(kryo: Kryo, input: Input, type: Class<DateTime>) = input.stream.readDateTime()
}