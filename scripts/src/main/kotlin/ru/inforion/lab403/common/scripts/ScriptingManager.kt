package ru.inforion.lab403.common.scripts

import ru.inforion.lab403.common.scripts.AbstractScriptEngine.Companion.toPowered
import javax.script.ScriptEngineManager

object ScriptingManager {
    private val manager = ScriptEngineManager()

    fun available() = manager.engineFactories.associate { it.engineName to it.names }

    fun engine(name: String) = manager.getEngineByName(name).apply {
        checkNotNull(this) { "Engine $name not supported" }
    }.toPowered()

    fun engine(name: String, snippets: List<String>) = engine(name).apply {
        snippets.forEach { eval(it) }
    }
}