package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.KryoRegistrator
import ru.inforion.lab403.common.kryo.registerByteBuffer
import ru.inforion.lab403.common.kryo.registerRanges
import ru.inforion.lab403.common.kryo.registerUnsigned

class BasicClassesRegistrator : KryoRegistrator {
    override fun registerClasses(kryo: Kryo) {
        kryo.registerUnsigned()
        kryo.registerRanges()
        kryo.registerByteBuffer()
    }
}