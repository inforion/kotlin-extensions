package ru.inforion.lab403.common.wsrpc.serde

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.scripts.GenericScriptEngine
import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.interfaces.Callable


class FunctionDeserializer(val server: WebSocketRpcServer) : JsonDeserializer<Callable<*>>() {
    companion object {
        val log = logger()
    }

    enum class FunctionType { FUNCTION, LAMBDA }

    data class FunctionDescription(
        val engine: String,
        val code: String,
        val type: FunctionType,
        val closure: Map<String, String>
    )

    private var lambdaIndex = 0

    private val engines = dictionaryOf<String, GenericScriptEngine>()

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Callable<*> {
        val desc = p.readValueAs(FunctionDescription::class.java)

        val engine = engines.getOrPut(desc.engine) { server.resources.checkoutScriptEngine(desc.engine) }

        val name = when (desc.type) {
            FunctionType.LAMBDA -> {
                desc.closure.forEach { (name, data) -> engine.deserializeAndSet(name, data.b64decode()) }
                engine.evalAndSet("anonymous${lambdaIndex++}", desc.code)
            }
            FunctionType.FUNCTION -> {
                log.severe { "Function closure variables currently not implemented!" }
                engine.evalGetNames(desc.code).first()
            }
        }

        return Callable<Any> { engine.invocable.invokeFunction(name, *it) }
    }

    internal fun onUnregister() {
        engines.values.forEach { server.resources.checkinScriptEngine(it) }
    }
}