package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.serializer.KryoSerializer
import org.junit.Test
import ru.inforion.lab403.common.extensions.int
import ru.inforion.lab403.common.extensions.split
import ru.inforion.lab403.common.extensions.uint

internal class UnsignedTest {
    class Testik(var data: UInt) {
        operator fun plus(other: UInt) = data + other

        operator fun plus(other: Testik) = data + other.data

        override fun toString() = "$data"
    }

    class TestikSerializer : Serializer<Testik>() {
        override fun write(kryo: Kryo, output: Output, obj: Testik) = output.writeInt(obj.data.int)
        override fun read(kryo: Kryo, input: Input, type: Class<Testik>) = Testik(input.readInt().uint)
    }

    class TestikRegistrator : KryoRegistrator {
        private val unsigned = BasicClassesRegistrator()

        override fun registerClasses(kryo: Kryo) {
            unsigned.registerClasses(kryo)
            kryo.register(Testik::class.java, TestikSerializer())
        }
    }

    @Test
    fun testUnsignedSerialize() {
        val sc = sparkContext(
            "test-unsigned",
            sparkSerializerConf(KryoSerializer::class, TestikRegistrator::class)
        )

        val list = List(100) { it.uint }

        val testik = Testik(100u)

        val partitions = list
            .split(sc.defaultParallelism())
            .map { it to testik }

        val result = partitions
            .parallelize(sc)
            .flatMap { (values, t) ->
                values.map { t + it * it }.iterator()
            }.collect()

//        val result = list.parallelize(sc).map { testik.value + it * it }.collect()

        println(result)

        println(result.first()::class.java)
    }
}