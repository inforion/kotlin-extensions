package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.json.toJson
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.wsrpc.descs.Parameters
import ru.inforion.lab403.common.wsrpc.interfaces.WebSocketRpcEndpoint
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.reflect.KParameter

class EndpointHolder constructor(
    val server: WebSocketRpcServer,
    val endpoint: WebSocketRpcEndpoint,
    val uuid: UUID
) {
    companion object {
        val log = logger()
    }

    val identifier get() = endpoint.name

    private val mapper = server.resources.checkoutJsonMapper()

    private val methods = server.resources.checkoutObjectMethods(endpoint::class)

    private fun Parameters.getValueOfParameter(parameter: KParameter): Any? {
        val name = parameter.name ?: return endpoint
        return getValue(name).fromJson(parameter.type, mapper)
    }

    internal fun execute(name: String, values: Parameters): String {
        val method = methods[name].sure { "Method $name was not found in $endpoint" }
        log.finer { "$this.${name}(${values.map { (key, value) -> "$key=$value" }.joinToString()})" }
        val args = method.parameters
            .filter { it.name == null || it.name in values }
            .associateWith { values.getValueOfParameter(it) }
        val result = method.function.callBy(args)
        if (method.close) server.unregister(uuid)
        return result.toJson(mapper)
    }

    internal fun onRegister() {
        // if required in future
    }

    internal fun onUnregister() {
        server.resources.checkinJsonMapper(mapper)
        server.resources.checkinObjectMethods()
    }

    override fun toString() = "$identifier[$uuid]"
}