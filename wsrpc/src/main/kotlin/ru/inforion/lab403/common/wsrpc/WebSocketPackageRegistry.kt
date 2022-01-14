package ru.inforion.lab403.common.wsrpc

import ru.inforion.lab403.common.extensions.dictionaryOf
import javax.script.ScriptEngine

class WebSocketPackageRegistry(init: WebSocketPackageRegistry.() -> Unit) {
    @PublishedApi
    internal var isServerInitialized = false

    private val context = dictionaryOf<String, Any>()

    internal fun applyPackages(engine: ScriptEngine) {
        isServerInitialized = true
        context.forEach { (name, pkg) ->
            engine.put(name, pkg)
        }
    }

    inline fun withCheck(action: () -> Unit) {
        require(!isServerInitialized) { "Server already initialized and types can't be registered" }
        action()
    }

    fun registerPackage(name: String, pkg: Any) = withCheck { context[name] = pkg }

    init {
        init(this)
    }
}