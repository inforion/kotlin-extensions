package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.serializer.KryoSerializer
import org.junit.Test
import ru.inforion.lab403.common.extensions.*

class Testik(val y: Int) {
    operator fun times(other: Testik) = y * other.y

    override fun toString() = "$y"
}

class TestikSerializer : Serializer<Testik>() {
    override fun write(kryo: Kryo, output: Output, obj: Testik) = output.writeInt(obj.y)
    override fun read(kryo: Kryo, input: Input, type: Class<Testik>) = Testik(input.readInt())
}

class UnsignedRegistrator : KryoRegistrator {
    override fun registerClasses(kryo: Kryo) {
        kryo.register(Testik::class.java, TestikSerializer())
        kryo.registerUnsigned()
    }
}

internal class UnsignedTest {
    @Test
    fun testUnsignedSerialize() {
        val sc = sparkContext(
            "test-unsigned",
            sparkSerializerConf(KryoSerializer::class, UnsignedRegistrator::class)
        )

        val list = List(100) { it.uint }

        val result = list.parallelize(sc).map { it * it }.collect()

        println(result)

        println(result.first()::class.java)
    }
}