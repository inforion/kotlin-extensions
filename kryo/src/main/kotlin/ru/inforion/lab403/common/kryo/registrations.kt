@file:Suppress("unused")

package ru.inforion.lab403.common.kryo

import com.esotericsoftware.kryo.Kryo
import org.joda.time.DateTime
import java.nio.ByteBuffer


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

fun Kryo.registerDateTime() {
    register(DateTime::class.java, DateTimeSerializer)
}


