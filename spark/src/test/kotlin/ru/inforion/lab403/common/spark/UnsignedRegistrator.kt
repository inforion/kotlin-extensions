package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.KryoRegistrator

class UnsignedRegistrator : KryoRegistrator {
    override fun registerClasses(kryo: Kryo) {
        kryo.registerUnsigned()
    }
}