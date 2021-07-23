package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.serializer.KryoRegistrator
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

class UIntSerializer : Serializer<UInt>() {
    override fun write(kryo: Kryo, output: Output, obj: UInt) = output.writeInt(obj.int)
    override fun read(kryo: Kryo, input: Input, type: Class<UInt>) = input.readInt().uint
}

class ULongSerializer : Serializer<ULong>() {
    override fun write(kryo: Kryo, output: Output, obj: ULong) = output.writeLong(obj.long)
    override fun read(kryo: Kryo, input: Input, type: Class<ULong>) = input.readLong().ulong
}

class UnsignedRegistrator : KryoRegistrator {
    override fun registerClasses(kryo: Kryo) {
        kryo.register(Testik::class.java, TestikSerializer())
        kryo.register(UInt::class.java, UIntSerializer())
        kryo.register(ULong::class.java, ULongSerializer())
        kryo.register(org.apache.spark.sql.catalyst.InternalRow::class.java)
        kryo.register(Array<org.apache.spark.sql.catalyst.InternalRow>::class.java)
    }
}

internal class UnsignedTest {
    @Test
    fun testUnsignedSerialize() {
        val conf = SparkConf()
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .set("spark.kryo.registrationRequired", "true")
            .set("spark.kryo.registrator", "ru.inforion.lab403.common.spark.UnsignedRegistrator")
            .setMaster("local[4]")
            .setAppName("test-unsigned")

        val context = JavaSparkContext(conf)

        val list = List(100) { it.uint }

        val result = context.parallelize(list).map { it * it }.collect()

        println(result)

        println(result.first()::class.java)
    }
}