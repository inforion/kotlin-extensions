package ru.inforion.lab403.common.extensions.scripts

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

    inline fun <reified T> getInterface(obj: Any): T = invocable.getInterface(obj, T::class.java)

    inline fun <reified T> getObjectAs(name: String): T = getInterface(get(name))

    fun evalGetNames(script: String): Set<String> {
        val namesBefore = engineScope.keys.toSet()
        engine.eval(script)
        val namesAfter = engineScope.keys.toSet()
        return namesAfter - namesBefore
    }

    fun evalAndSet(name: String, script: String) = name.also { engine.put(it, eval(script)) }

    abstract fun deserialize(bytes: ByteArray): Any

    fun deserializeAndSet(name: String, bytes: ByteArray) = name.also { put(it, deserialize(bytes)) }

    inline fun <reified T> deserializeAsInterface(bytes: ByteArray) = getInterface<T>(deserialize(bytes))
}