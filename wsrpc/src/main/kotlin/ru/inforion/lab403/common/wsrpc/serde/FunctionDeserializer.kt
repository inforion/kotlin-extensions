package ru.inforion.lab403.common.wsrpc.serde

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.inforion.lab403.common.extensions.availableProcessors
import ru.inforion.lab403.common.json.deserialize
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.scripts.ScriptingManager
import ru.inforion.lab403.common.wsrpc.WebSocketPackageRegistry
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import java.lang.reflect.Type
import java.util.concurrent.LinkedBlockingQueue


internal class FunctionDeserializer(val registry: WebSocketPackageRegistry) : JsonDeserializer<Callable<*>> {
    companion object {
        val log = logger()
    }

    enum class FunctionType { FUNCTION, LAMBDA }

    data class FunctionDescription(
        val engine: String,
        val code: String,
        val type: FunctionType,
        val name: String?,
        val closure: Map<String, ByteArray>
    )

    private var lambdaIndex = 0

    private inline fun <T> Collection<T>.toLinkedBlockingQueue() = LinkedBlockingQueue(this)

    init {
        log.info { "Create new engine pool" }
    }

    private inline fun <E, T> LinkedBlockingQueue<E>.acquire(action: E.() -> T): T {
        val engine = take()
        return action(engine).also { put(engine) }
    }

    private val engines by lazy {
        List(availableProcessors) {
            ScriptingManager.engine("python").also { registry.applyPackages(it) }
        }.toLinkedBlockingQueue()
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Callable<*> {
        val desc = json.deserialize<FunctionDescription>(context)

        val index = lambdaIndex++
        val name = desc.name ?: "anonymous$index"

        return engines.acquire {
            when (desc.type) {
                FunctionType.LAMBDA -> {
                    desc.closure.forEach { (name, data) -> deserializeAndSet(name, data) }
                    evalAndSet(name, desc.code)
                }
                FunctionType.FUNCTION -> {
                    log.severe { "Function closure variables currently not implemented!" }
                    evalGetNames(desc.code)
                }
            }
            Callable<Any> { invocable.invokeFunction(name, *it) }
        }
    }
}