package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.extensions.firstInstance
import ru.inforion.lab403.common.extensions.hasInstance
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.*
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.reflection.kClassAny
import ru.inforion.lab403.common.wsrpc.WebSocketTypes.registerModule
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.descs.Parameters
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.wsrpc.serde.FunctionDeserializer
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.javaType

class EndpointHolder constructor(
    val server: WebSocketRpcServer,
    val endpoint: WebSocketRpcEndpoint,
    val uuid: UUID
) {
    companion object {
        val log = logger()
    }

    private data class Method<R>(
        val function: KFunction<R>,
        val parameters: List<KParameter>,
        val close: Boolean)

    val identifier = endpoint.name

    private val functionDeserializer = FunctionDeserializer(server)

    private val mapper = defaultJsonBuilder()
        .registerBasicClasses()
        .registerTypeAdapter(Callable::class, functionDeserializer)
        .registerModule(server)
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
        return getValue(name).fromJson(parameter.type, mapper)
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