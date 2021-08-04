package ru.inforion.lab403.common.json

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.junit.Test
import java.lang.reflect.Type
import java.util.*
import kotlin.test.assertEquals

internal class GsonJsonTest {

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

    class ObjectSerializer : JsonSerializer<Iterator<*>> {
        data class ObjectDescription(val __rpc__: String, val endpoint: UUID)

        override fun serialize(src: Iterator<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
//            val holder = server.register(apiGen(src))
            return context.serialize(ObjectDescription("111", UUID.randomUUID()))
        }
    }

    @Test
    fun sequenceCustomSerializerTest() {
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .registerTypeAdapter(Iterator::class.java, ObjectSerializer())
            .create()

        var sequence = List(100) { it }.asIterable()

        sequence = sequence.filter {
            println("1. it = $it")
            it < 100
        }

        sequence = sequence.map {
            println("2. it = $it")
            it + it
        }

        val json = sequence.toJson(gson)
        println(json)
        println(sequence.joinToString())
    }
}