package ru.inforion.lab403.common.scripts

import org.jetbrains.kotlin.cli.common.environment.setIdeaIoUseFallback
import ru.inforion.lab403.common.extensions.cast
import ru.inforion.lab403.common.extensions.isWindowsOperatingSystem
import kotlin.script.experimental.jvmhost.jsr223.KotlinJsr223ScriptEngineImpl

class KotlinScriptEngine(
    val engine: KotlinJsr223ScriptEngineImpl
) : AbstractScriptEngine<KotlinJsr223ScriptEngineImpl>(engine) {

    init {
        // See https://stackoverflow.com/questions/61438356/runtime-compilation-of-kotlin-code-throws-runtimeexception-warn-failed-to-init
        if (isWindowsOperatingSystem) setIdeaIoUseFallback()
    }

    override val name = "kotlin"

    override fun serialize(value: Any) = byteArrayOf()

    override fun deserialize(bytes: ByteArray) = Any()

    override fun <T> toInterface(obj: Any, cls: Class<out T>, default: T?): T {
        require(cls.isInterface) { "interface expected" }
        @Suppress("UNCHECKED_CAST")
        return engine.eval(obj.cast<String>()) as T
    }
}
