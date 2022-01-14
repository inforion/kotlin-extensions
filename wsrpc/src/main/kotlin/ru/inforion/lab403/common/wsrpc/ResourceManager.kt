package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.extensions.dictionaryOf
import ru.inforion.lab403.common.extensions.firstInstance
import ru.inforion.lab403.common.extensions.hasInstance
import ru.inforion.lab403.common.json.Json
import ru.inforion.lab403.common.json.defaultJsonBuilder
import ru.inforion.lab403.common.json.registerTypeAdapter
import ru.inforion.lab403.common.wsrpc.annotations.WebSocketRpcMethod
import ru.inforion.lab403.common.wsrpc.interfaces.Callable
import ru.inforion.lab403.common.wsrpc.serde.FunctionDeserializer
import java.util.concurrent.LinkedBlockingQueue
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions

internal class ResourceManager(
    private val server: WebSocketRpcServer,
    private val types: WebSocketTypesRegistry,
    private val packages: WebSocketPackageRegistry
) {
    private val mappers = LinkedBlockingQueue<Json>()

    fun checkoutJsonMapper(): Json = mappers.poll() ?: defaultJsonBuilder()
        .registerTypeAdapter(Callable::class, FunctionDeserializer(packages))
        .apply { types.setupJsonBuilder(this, server) }
        .create()

    fun checkinJsonMapper(mapper: Json) = mappers.offer(mapper)

    internal data class Method<R>(
        val function: KFunction<R>,
        val parameters: List<KParameter>,
        val close: Boolean
    )

    private val methods = dictionaryOf<KClass<*>, Map<String, Method<*>>>()

    fun checkoutObjectMethods(kClass: KClass<*>) = methods.getOrPut(kClass) {
        kClass.memberFunctions
            .filter { it.annotations.hasInstance(WebSocketRpcMethod::class.java) }
            .associate { func ->
                val autoClose = func.annotations.firstInstance(WebSocketRpcMethod::class.java).close
                func.name to Method(func, func.parameters, autoClose)
            }
    }

    fun checkinObjectMethods() = Unit
}