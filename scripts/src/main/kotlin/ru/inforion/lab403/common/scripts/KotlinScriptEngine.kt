package ru.inforion.lab403.common.scripts

import org.jetbrains.kotlin.cli.common.environment.setIdeaIoUseFallback
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.jetbrains.kotlin.utils.addToStdlib.cast
import ru.inforion.lab403.common.extensions.isWindowsOperatingSystem

class KotlinScriptEngine(
    val engine: KotlinJsr223JvmLocalScriptEngine
) : AbstractScriptEngine<KotlinJsr223JvmLocalScriptEngine>(engine) {

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