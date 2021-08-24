package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.KryoRegistrator

class BasicClassesRegistrator : KryoRegistrator {
    override fun registerClasses(kryo: Kryo) {
        kryo.registerUnsigned()
        kryo.registerRanges()
        kryo.registerByteBuffer()
    }
}