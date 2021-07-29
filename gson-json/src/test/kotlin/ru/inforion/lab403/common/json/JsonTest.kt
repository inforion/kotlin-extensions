package ru.inforion.lab403.common.json

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.junit.Test
import kotlin.test.assertEquals

internal class JsonTest {

    class UIntAdapter : TypeAdapter<UInt>() {
        override fun write(out: JsonWriter, value: UInt) {
            out.value(value.toInt())
        }

        override fun read(`in`: JsonReader): UInt {
            return `in`.nextInt().toUInt()
        }
    }

//    class TestikDeserializer : JsonDeserializer<Testik> {
//        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Testik {
//            TODO("Not yet implemented")
//        }
//    }

    data class Testik(val data: UInt)

    @Test
    fun unsignedTest() {
        val expected = Testik(0xFFFF_FFFFu)
        val json = expected.toJson()

        println(json)

        val actual = json.fromJson<Testik>()

        println(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun unsignedOverflowTest() {
//        val actual = """{"data":${UInt.MAX_VALUE}}""".fromJson<Testik>()
//        assertEquals(Testik(UInt.MAX_VALUE), actual)
    }

    @Test
    fun anySerializationTest() {
        fun getAny(): Any = Testik(100u)

        val obj1 = getAny()
        val json = obj1.toJson()
        println(json)
        val obj2 = json.fromJson<Testik>()

        assertEquals(obj2, obj1)
    }
}