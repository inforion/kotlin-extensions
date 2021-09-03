package ru.inforion.lab403.common.scripts

import org.python.jsr223.PyScriptEngine
import javax.script.Bindings
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.ScriptEngine

abstract class AbstractScriptEngine<E : ScriptEngine>(private val engine: E) : ScriptEngine by engine {
    companion object {
        fun ScriptEngine.toPowered(): GenericScriptEngine = when (this) {
            is PyScriptEngine -> PythonScriptEngine(this)
            else -> throw NotImplementedError("Engine $this was not powered!")
        }
    }

    abstract val name: String

    val engineScope: Bindings get() = engine.context.getBindings(ScriptContext.ENGINE_SCOPE)

    val globalScope: Bindings get() = engine.context.getBindings(ScriptContext.GLOBAL_SCOPE)

    val invocable = engine as Invocable

    abstract fun <T> toInterface(obj: Any, cls: Class<out T>, default: T?): T

    fun evalGetNames(script: String): Set<String> {
        val namesBefore = engineScope.keys.toSet()
        engine.eval(script)
        val namesAfter = engineScope.keys.toSet()
        return namesAfter - namesBefore
    }

    fun evalAndSet(name: String, script: String) = name.also { put(it, eval(script)) }

    abstract fun serialize(value: Any): ByteArray

    abstract fun deserialize(bytes: ByteArray): Any

    fun deserializeAndSet(name: String, bytes: ByteArray) = name.also { put(it, deserialize(bytes)) }

    inline fun <reified T> deserializeAsInterface(bytes: ByteArray) =
        toInterface(deserialize(bytes), T::class.java, null)

    inline fun <reified T> deserializeWithDefaultImpl(bytes: ByteArray, default: T) =
        toInterface(deserialize(bytes), T::class.java, default)
}