package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.extensions.otherwise
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.logging.Levels
import ru.inforion.lab403.common.logging.Messenger
import ru.inforion.lab403.common.logging.logger.Config.toPublisher
import java.io.File

object ConfigFileLoader {
    private const val ENV_CONF_PATH = "INFORION_LOGGING_CONF_PATH"
    private const val ENV_DEBUG_ENABLED = "INFORION_LOGGING_PRINT"
    private const val DEFAULT_LEVEL = "INFO"

    private inline fun <T> info(message: Messenger<T>) = System.err.println(message().toString())

    private inline fun <T> debug(message: Messenger<T>) {
        if (isDebugEnabled) info(message)
    }

    private val isDebugEnabled by lazy {
        env(ENV_DEBUG_ENABLED) ifNotNull { toBoolean() } either { false }
    }


    private fun env(name: String): String? = System.getenv(name).also {
        if (it != null) info { "$name: $it" }
    }

    private val configurations: File? by lazy {
        val path = env(ENV_CONF_PATH)

        if (path != null) {
            File(path).takeIf { it.isFile } otherwise {
                info { "Logging configuration file can't be loaded: $this" }
            }
        } else {
            debug { "INFORION_LOGGING_CONF_PATH not specified" }; null
        }
    }

    fun getConfig() = configurations ifNotNull {
        runCatching {
            fromJson<Map<String, Config.LoggerInfo>>().mapValues {
                val loggerInfo = it.value
                Config.LoggerRuntimeInfo(Levels.valueOf(loggerInfo.level ?: DEFAULT_LEVEL).level,
                    loggerInfo.publishers
                            ifNotNull { map { it.toPublisher() }.toMutableList() }
                            either { mutableListOf() })
            }
        }.onSuccess {
            info { "Successfully loading logger configuration file '$this'" }
        }.onFailure { error ->
            info { "Can't parse logger configuration file '$this' due to $error" }
        }.getOrNull()
    } either {
        emptyMap()
    }
}