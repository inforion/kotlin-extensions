package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.inforion.lab403.common.extensions.b64decode
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.scripts.GenericScriptEngine
import ru.inforion.lab403.common.wsrpc.WebSocketRpcServer
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import java.lang.reflect.Type


class FunctionDeserializer(val server: WebSocketRpcServer) : JsonDeserializer<Callable<*>> {
    companion object {
        val log = logger()
    }

    enum class FunctionType { FUNCTION, LAMBDA }

    internal data class FunctionDescription(
        val engine: String,
        val code: String,
        val type: FunctionType,
        val closure: Map<String, ByteArray>
    )

    private var lambdaIndex = 0

    private val engines = dictionaryOf<String, GenericScriptEngine>()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Callable<*> {
        val desc = json.deserialize<FunctionDescription>(context)

        val engine = engines.getOrPut(desc.engine) { server.resources.checkoutScriptEngine(desc.engine) }

        val name = when (desc.type) {
            FunctionType.LAMBDA -> {
                desc.closure.forEach { (name, data) -> engine.deserializeAndSet(name, data) }
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