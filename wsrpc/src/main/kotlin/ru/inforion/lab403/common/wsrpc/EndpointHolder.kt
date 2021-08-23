package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.extensions.firstInstance
import ru.inforion.lab403.common.extensions.hasInstance
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.json.toJson
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.descs.Parameters
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
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
    }

    private data class Method<R>(
        val function: KFunction<R>,
        val parameters: List<KParameter>,
        val close: Boolean)

    val identifier = endpoint.name

    private val mapper = server.resources.checkoutJsonMapper()

    private val methods = endpoint::class.memberFunctions
        .filter { it.annotations.hasInstance(WebSocketRpcMethod::class.java) }
        .associate { func ->
            val autoClose = func.annotations.firstInstance(WebSocketRpcMethod::class.java).close
            func.name to Method(func, func.parameters, autoClose)
        }

    private fun Parameters.getValueOfParameter(parameter: KParameter): Any? {
        val name = parameter.name ?: error("Something wrong with signature of RPC function -> parameter has no name")
        require(parameter.name in this) { "Required parameter $name not found in received data" }
        return getValue(name).fromJson(parameter.type, mapper)
    }

    internal fun execute(name: String, values: Parameters): String {
        val method = methods[name].sure { "Method $name was not found in $endpoint" }
        log.finer { "$this.${name}(${values.map { (key, value) -> "$key=$value" }.joinToString()})" }
        val args = method.parameters
            .filter { it.name in values || !it.isOptional }  // leave parameter in list if it is not optional to throw an error
            .associateWith { if (it.name == null) endpoint else values.getValueOfParameter(it) }
        val result = method.function.callBy(args)
        if (method.close) server.unregister(uuid)
        return result.toJson(mapper)
    }

    internal fun onRegister() {
        // if required in future
    }

    internal fun onUnregister() {
        server.resources.checkinJsonMapper(mapper)
    }

    override fun toString() = "$identifier[$uuid]"
}