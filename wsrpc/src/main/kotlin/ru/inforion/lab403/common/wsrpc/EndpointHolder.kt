package ru.inforion.lab403.common.wsrpc

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import ru.inforion.lab403.common.extensions.firstInstance
import ru.inforion.lab403.common.extensions.hasInstance
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.*
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import ru.inforion.lab403.common.wsrpc.serde.WebSocketRpcJacksonModule
import java.util.*
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

    private val module = WebSocketRpcJacksonModule(server)

    private val mapper = Json {
        serializersModule = module.build()
    }

    private val methods = endpoint::class.memberFunctions
        .filter { it.annotations.hasInstance(WebSocketRpcMethod::class.java) }
        .associate { func ->
            val parameters = func.parameters.filter { it.name != null }
            val autoClose = func.annotations.firstInstance(WebSocketRpcMethod::class.java).close
            func.name to Method(func, parameters, autoClose)
        }

    private fun Map<String, JsonElement>.getValueOfParameter(parameter: KParameter) =
        getValue(parameter.name!!).parseJson(mapper, parameter.type)

    internal fun execute(name: String, values: Map<String, JsonElement>): String {
        val method = methods[name].sure { "Method $name was not found in $endpoint" }
//        log.finer { "$this.${name}(${values.map { (key, value) -> "$key=${value?.toString()}" }.joinToString()})" }
        val args = method.parameters.map { values.getValueOfParameter(it) }
        val result = method.function.call(endpoint, *args.toTypedArray())
        if (method.close) server.unregister(uuid)
        return result.writeJson(mapper)
    }

    internal fun onRegister() {
        // if required in future
    }

    internal fun onUnregister() {
        module.onUnregister()
    }

    override fun toString() = "$identifier[$uuid]"
}