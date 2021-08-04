package ru.inforion.lab403.common.json

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.junit.Test
import kotlin.test.assertEquals

internal class JacksonJsonTest {

//    class TestikDeserializer : JsonDeserializer<Testik> {
//        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Testik {
//            TODO("Not yet implemented")
//        }
//    }


    @Test
    fun simpleUnsignedTest() {
        val json = 0xFFFF_FFFFu.toJson()

        println(json)

        val actual = json.fromJson<UInt>()

        println(actual)
        assertEquals(0xFFFF_FFFFu, actual)
    }

    object UIntSerializer: StdSerializer<UInt>(UInt::class.java) {
        override fun serialize(value: UInt, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeNumber(value.toLong())
        }
    }

    object UIntDeserializer: StdDeserializer<UInt>(UInt::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UInt {
            return p.longValue.toUInt()
        }
    }

    object InlineModule : SimpleModule("Inline") {
        override fun setupModule(context: SetupContext) {
            super.setupModule(context)
            context.appendAnnotationIntrospector(InlineAnnotationIntrospector)
        }

        object InlineAnnotationIntrospector : NopAnnotationIntrospector() {
            override fun findCreatorAnnotation(config: MapperConfig<*>, a: Annotated): JsonCreator.Mode? {
//                if (a is AnnotatedMethod && a.name == "box-impl") {
                    return JsonCreator.Mode.PROPERTIES
//                }
//                return null
            }
        }
    }

    @Test
    fun fieldUnsignedTest() {
        val module = SimpleModule()
            .addSerializer(UIntSerializer)
            .addDeserializer(Comparable::class.java, UIntDeserializer)

        val mapper = jsonParser()
//            .registerModule(module)
//            .registerModule(InlineModule)

        val expected = Testik(0xFFFF_FFFFu)
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

    class SequenceSerializer : JsonSerializer<Sequence<*>>() {
        data class ObjectDescriptor(val __rpc__: String, val id: Int)

        override fun serialize(value: Sequence<*>, gen: JsonGenerator, serializers: SerializerProvider) {
            val descriptor = ObjectDescriptor("Sequence", value.hashCode())
            gen.writeObject(descriptor)
        }
    }

    @Test
    fun sequenceCustomSerializerTest() {
        val sequenceModule = SimpleModule()
            .addSerializer(SequenceSerializer())

        val gson = jsonParser()
            .registerModule(sequenceModule)

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