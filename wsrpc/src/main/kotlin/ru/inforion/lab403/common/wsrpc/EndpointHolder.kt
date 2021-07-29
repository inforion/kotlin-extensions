package ru.inforion.lab403.common.wsrpc

import com.google.gson.GsonBuilder
import ru.inforion.lab403.common.concurrent.events.Event
import ru.inforion.lab403.common.extensions.*
import ru.inforion.lab403.common.json.*
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.descs.Parameters
import ru.inforion.lab403.common.wsrpc.endpoints.EventEndpoint
import ru.inforion.lab403.common.wsrpc.endpoints.SequenceEndpoint
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.wsrpc.serde.FunctionDeserializer
import ru.inforion.lab403.common.wsrpc.serde.ObjectSerializer
import ru.inforion.lab403.common.wsrpc.serde.registerBasicClasses
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions

class EndpointHolder constructor(
    val server: WebSocketRpcServer,
    val endpoint: WebSocketRpcEndpoint,
    val uuid: UUID
) {
    companion object {
        val log = logger()

        private fun <T: Any> GsonBuilder.registerTypeAdapter(
            server: WebSocketRpcServer,
            kClass: KClass<T>,
            apiGen: (T) -> WebSocketRpcEndpoint
        ) = registerTypeAdapter(kClass, ObjectSerializer(server, kClass, apiGen))

        fun GsonBuilder.registerModule(server: WebSocketRpcServer): GsonBuilder {
            WebSocketRpcServer.serializers.forEach { (cls, gen) -> registerTypeAdapter(server, cls, gen) }

            registerTypeAdapter(server, Sequence::class) { SequenceEndpoint(it) }
            registerTypeAdapter(server, SequenceEndpoint::class) { it }

            registerTypeAdapter(server, Event::class) { EventEndpoint(it) }
            registerTypeAdapter(server, EventEndpoint::class) { it }

            return this
        }
    }

    private data class Method<R>(
        val function: KFunction<R>,
        val parameters: List<KParameter>,
        val close: Boolean)

    val identifier = endpoint.name

    private val functionDeserializer = FunctionDeserializer(server)

    private val mapper = GsonBuilder()
        .registerBasicClasses()
        .registerModule(server)
        .registerTypeAdapter(Callable::class, functionDeserializer)
        .serializeNulls()
        .create()

    private val methods = endpoint::class.memberFunctions
        .filter { it.annotations.hasInstance(WebSocketRpcMethod::class.java) }
        .associate { func ->
            val parameters = func.parameters.filter { it.name != null }
            val autoClose = func.annotations.firstInstance(WebSocketRpcMethod::class.java).close
            func.name to Method(func, parameters, autoClose)
        }

    private fun Parameters.getValueOfParameter(parameter: KParameter): Any? {
        val name = parameter.name ?: error("Something wrong with signature of RPC function -> parameter has no name")
        require(parameter.name in this) { "Required parameter $name not found in received data" }
        val value = getValue(name)
        return mapper.fromJson(value, parameter.type.javaClass)
    }

    internal fun execute(name: String, values: Parameters): String {
        val method = methods[name].sure { "Method $name was not found in $endpoint" }
        log.finer { "$this.${name}(${values.map { (key, value) -> "$key=$value" }.joinToString()})" }
        val args = method.parameters.map { values.getValueOfParameter(it) }
        val result = method.function.call(endpoint, *args.toTypedArray())
        if (method.close) server.unregister(uuid)
        return result.toJson(mapper)
    }

    internal fun onRegister() {
        // if required in future
    }

    internal fun onUnregister() {
        functionDeserializer.onUnregister()
    }

    override fun toString() = "$identifier[$uuid]"
}