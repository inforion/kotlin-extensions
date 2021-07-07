package ru.inforion.lab403.common.wsrpc.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.extensions.b64encode

class ByteArrayDescriptor(val __bytes__: String) {
    object Serializer : JsonSerializer<ByteArray>() {
        override fun serialize(value: ByteArray, gen: JsonGenerator, serializers: SerializerProvider) {
            val descriptor = ByteArrayDescriptor(value.b64encode())
            gen.writeObject(descriptor)
        }
    }

    object Deserializer : JsonDeserializer<ByteArray>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ByteArray {
            val descriptor = p.readValueAs(ByteArrayDescriptor::class.java)
            return descriptor.__bytes__.b64decode()
        }
    }
}
