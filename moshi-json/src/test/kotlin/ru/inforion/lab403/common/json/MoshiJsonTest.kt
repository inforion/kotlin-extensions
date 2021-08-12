package ru.inforion.lab403.common.json

import com.squareup.moshi.*
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals


internal class MoshiJsonTest {

    object UIntAdapter : JsonAdapter<UInt>() {
        override fun fromJson(reader: JsonReader) = reader.nextInt().toUInt()

        override fun toJson(writer: JsonWriter, value: UInt?) {
            writer.value(value?.toInt())
        }
    }

    object ObjectSerializer : JsonAdapter<Iterator<*>>() {
        data class ObjectDescription(val __rpc__: String, val endpoint: UUID)

        override fun fromJson(reader: JsonReader) = throw NotImplementedError()

        override fun toJson(writer: JsonWriter, value: Iterator<*>?) {
            writer.jsonValue(ObjectDescription("RPC", UUID.randomUUID()))
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
        val mapper = defaultJsonBuilder()
            .addLast(UInt::class.java, UIntAdapter)
            .create()

        val expected = Testik(1u)
        val json = expected.toJson(mapper)

        println(json)

        val actual = json.fromJson<Testik>(mapper)

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

    abstract class AbstractAnimal(val name: String, val type: String)

    data class Cat(val cat: String) : AbstractAnimal("pushok", "Cat")

    data class Dog(val dog: String) : AbstractAnimal("bobik", "Dog")

    @Test
    fun polymorphicDeserializationTest() {
        val gson = defaultJsonBuilder()
            .add(polymorphicTypesFactory(listOf(Cat::class.java, Dog::class.java)))
            .create()

        val string = """ { "cat": "meow", "type": "Cat" } """

//        val string = expected.toJson(gson)

        println(string)

        val cat = string.fromJson<AbstractAnimal>(gson) as Cat

        println(cat.type)
        println(cat.name)
        println(cat.cat)
    }

    @Test
    fun sequenceCustomSerializerTest() {
        val gson = defaultJsonBuilder()
            .add(Iterator::class.java, ObjectSerializer)
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