@file:Suppress("unused")

package ru.inforion.lab403.common.wsrpc

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapterFactory
import ru.inforion.lab403.common.concurrent.events.Event
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.json.*
import ru.inforion.lab403.common.json.dontimport.identifierOrName
import ru.inforion.lab403.common.json.interfaces.JsonSerde
import ru.inforion.lab403.common.wsrpc.endpoints.EventEndpoint
import ru.inforion.lab403.common.wsrpc.endpoints.SequenceEndpoint
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.wsrpc.serde.ByteArraySerializer
import ru.inforion.lab403.common.wsrpc.serde.ObjectSerializer
import java.lang.reflect.Type
import kotlin.reflect.KClass

object WebSocketTypes {
    @PublishedApi internal val endpointsSerializers = dictionaryOf<KClass<Any>, (Any) -> WebSocketRpcEndpoint>()

    @PublishedApi internal val typesSerializers = mutableListOf<Pair<Type, Any>>()

    @PublishedApi internal val typesFactories = mutableListOf<TypeAdapterFactory>()

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> registerTypeAdapter(kClass: KClass<out T>, apiGen: (T) -> WebSocketRpcEndpoint) {
        check(kClass as KClass<Any> !in endpointsSerializers) {
            "Can't set global serializer for class $kClass because it was already specified"
        }
        endpointsSerializers[kClass] = apiGen as (Any) -> WebSocketRpcEndpoint
    }

    fun <T: Any> registerTypeAdapter(kClass: KClass<T>, deserializer: JsonDeserializer<T>) =
        typesSerializers.add(kClass.java to deserializer)

    fun <T: Any> registerTypeAdapter(kClass: KClass<T>, serializer: JsonSerializer<T>) =
        typesSerializers.add(kClass.java to serializer)

    fun <T: Any> registerTypeAdapter(kClass: KClass<T>, serde: JsonSerde<T>) =
        typesSerializers.add(kClass.java to serde)

    fun registerTypeFactory(factory: TypeAdapterFactory) = typesFactories.add(factory)

    inline fun <reified T: Any> registerPolymorphicAdapter(
        classes: Collection<Class<out T>>,
        field: String = "type",
        selector: (Class<out T>) -> String = { it.identifierOrName }
    ) = typesSerializers.add(T::class.java to polymorphicTypesAdapter(classes, field, selector))

    fun JsonBuilder.registerModule(server: WebSocketRpcServer) = apply {
        typesSerializers.forEach { registerTypeAdapter(it.first, it.second) }
        typesFactories.forEach { registerTypeAdapterFactory(it) }

        endpointsSerializers.forEach { (cls, gen) ->
            registerTypeAdapter(cls, ObjectSerializer(server, cls, gen))
        }

        registerTypeAdapter(SequenceEndpoint::class, ObjectSerializer(server) { it })
        registerTypeAdapter(Event::class, ObjectSerializer(server) { EventEndpoint(it) })
        registerTypeAdapter(EventEndpoint::class, ObjectSerializer(server) { it })

        registerTypeAdapter(ByteArray::class, ByteArraySerializer)
    }
}