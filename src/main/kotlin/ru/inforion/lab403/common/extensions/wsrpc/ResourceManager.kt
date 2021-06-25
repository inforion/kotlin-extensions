package ru.inforion.lab403.common.extensions.wsrpc

import ru.inforion.lab403.common.extensions.availableProcessors
import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.extensions.scripts.GenericScriptEngine
import ru.inforion.lab403.common.extensions.scripts.ScriptingManager
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class ResourceManager {
    private val engines = dictionaryOf<String, Queue<GenericScriptEngine>>()

    fun checkoutScriptEngine(name: String) = engines
        .getOrPut(name) { LinkedBlockingQueue(availableProcessors) }
        .poll() ?: ScriptingManager.engine(name)

    fun checkinScriptEngine(engine: GenericScriptEngine) = engines
        .getValue(engine.name)
        .offer(engine)
}