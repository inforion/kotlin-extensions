package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.extensions.availableProcessors
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.json.Json
import ru.inforion.lab403.common.json.defaultJsonBuilder
import ru.inforion.lab403.common.json.registerTypeAdapter
import ru.inforion.lab403.common.scripts.GenericScriptEngine
import ru.inforion.lab403.common.scripts.ScriptingManager
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import ru.inforion.lab403.common.wsrpc.serde.FunctionDeserializer
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class ResourceManager(private val server: WebSocketRpcServer, private val registry: WebSocketTypesRegistry) {
    private val engines = dictionaryOf<String, Queue<GenericScriptEngine>>()

    fun checkoutScriptEngine(name: String) = engines
        .getOrPut(name) { LinkedBlockingQueue(availableProcessors) }
        .poll() ?: ScriptingManager.engine(name)

    fun checkinScriptEngine(engine: GenericScriptEngine) = engines
        .getValue(engine.name)
        .offer(engine)

    private val mappers = LinkedBlockingQueue<Json>()

    fun checkoutJsonMapper(): Json = mappers.poll() ?: defaultJsonBuilder()
        .registerTypeAdapter(Callable::class, FunctionDeserializer(this))
        .apply { registry.setupJsonBuilder(this, server) }
        .create()

    fun checkinJsonMapper(mapper: Json) = mappers.offer(mapper)
}