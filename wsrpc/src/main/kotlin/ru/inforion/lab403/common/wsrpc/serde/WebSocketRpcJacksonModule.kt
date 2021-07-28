package ru.inforion.lab403.common.wsrpc.serde

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import ru.inforion.lab403.common.concurrent.events.Event
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.endpoints.EventEndpoint
import ru.inforion.lab403.common.wsrpc.endpoints.SequenceEndpoint
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import kotlin.reflect.KClass

class WebSocketRpcJacksonModule constructor(private val server: WebSocketRpcServer) {
    companion object {
        private val serializers = dictionaryOf<KClass<*>, (Any) -> WebSocketRpcEndpoint>()

        fun <T: Any> mapping(kClass: KClass<T>, apiGen: (T) -> WebSocketRpcEndpoint) {
            check(kClass !in serializers) {
                "Can't set global serializer for class $kClass because it was already specified"
            }
            @Suppress("UNCHECKED_CAST")
            serializers[kClass] = apiGen as (Any) -> WebSocketRpcEndpoint
        }
    }

    fun <T: Any> SerializersModuleBuilder.mapping(kClass: KClass<T>, apiGen: (T) -> WebSocketRpcEndpoint) =
        contextual(kClass, ObjectSerializer(server, kClass, apiGen))

    private val functionDeserializer = FunctionDeserializer(server)

    internal fun onUnregister() = functionDeserializer.onUnregister()

    fun build() = SerializersModule {
        serializers.forEach { (cls, gen) -> mapping(cls, gen) }

        mapping(Sequence::class) { SequenceEndpoint(it) }
        mapping(SequenceEndpoint::class) { it }

        mapping(Event::class) { EventEndpoint(it) }
        mapping(EventEndpoint::class) { it }

        contextual(ByteArray::class, ByteArrayDescriptor.Serde)

        contextual(Callable::class, functionDeserializer)
    }
}