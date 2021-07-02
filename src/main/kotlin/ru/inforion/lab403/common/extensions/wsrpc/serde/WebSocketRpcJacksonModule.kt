package ru.inforion.lab403.common.extensions.wsrpc.serde

import com.fasterxml.jackson.databind.module.SimpleModule
import ru.inforion.lab403.common.extensions.concurrent.events.Event
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.extensions.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.extensions.wsrpc.endpoints.EventEndpoint
import ru.inforion.lab403.common.extensions.wsrpc.endpoints.SequenceEndpoint
import ru.inforion.lab403.common.extensions.wsrpc.interfaces.Callable
import ru.inforion.lab403.common.extensions.wsrpc.interfaces.WebSocketRpcEndpoint
import kotlin.reflect.KClass

class WebSocketRpcJacksonModule constructor(val server: WebSocketRpcServer) : SimpleModule("RpcJacksonModule") {
    companion object {
        private val serializers = dictionaryOf<KClass<*>, (Any) -> WebSocketRpcEndpoint>()

        fun <T: Any> addMapping(kClass: KClass<T>, apiGen: (T) -> WebSocketRpcEndpoint) {
            check(kClass !in serializers) {
                "Can't set global serializer for class $kClass because it was already specified"
            }
            @Suppress("UNCHECKED_CAST")
            serializers[kClass] = apiGen as (Any) -> WebSocketRpcEndpoint
        }
    }

    fun <T: Any> addMapping(kClass: KClass<T>, apiGen: (T) -> WebSocketRpcEndpoint): SimpleModule =
        addSerializer(kClass.java, ObjectSerializer(server, kClass, apiGen))

    private val functionDeserializer = FunctionDeserializer(server)

    internal fun onUnregister() = functionDeserializer.onUnregister()

    init {
        serializers.forEach { (cls, gen) -> addMapping(cls, gen) }

        addMapping(Sequence::class) { SequenceEndpoint(it) }
        addMapping(SequenceEndpoint::class) { it }

        addMapping(Event::class) { EventEndpoint(it) }
        addMapping(EventEndpoint::class) { it }

        addSerializer(ByteArray::class.java, ByteArrayDescriptor.Serializer)
        addDeserializer(ByteArray::class.java, ByteArrayDescriptor.Deserializer)

        addDeserializer(Callable::class.java, functionDeserializer)
    }
}