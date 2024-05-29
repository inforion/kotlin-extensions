package ru.inforion.lab403.common.logging.config

import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.extensions.otherwise
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.logging.Messenger
import ru.inforion.lab403.common.logging.logLevel
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import java.io.File

class LoggerFileConfigInitializer : ILoggerConfigInitializer {
    companion object {
        const val ENV_CONF_PATH = "INFORION_LOGGING_CONF_PATH"
        const val ENV_DEBUG_ENABLED = "INFORION_LOGGING_PRINT"
    }

    private inline fun <T> info(message: Messenger<T>) = LoggerStorage.fallbackPublishers.forEach {
        it.publish("LoggerFileConfigInitializer: " + message().toString())
    }

    private inline fun <T> debug(message: Messenger<T>) {
        if (isDebugEnabled) info(message)
    }

    private val isDebugEnabled by lazy {
        env(ENV_DEBUG_ENABLED) ifNotNull { toBoolean() } either { false }
    }

    private fun env(name: String): String? = System.getenv(name).also {
        if (it != null) info { "$name: $it" }
    }

    private val configFile: File? by lazy {
        val path = env(ENV_CONF_PATH)

        if (path != null) {
            File(path).takeIf { it.isFile } otherwise {
                info { "Logging configuration file '$path' can't be loaded: $this" }
            }
        } else {
            debug { "$ENV_CONF_PATH not specified" }; null
        }
    }

    override fun load() {
        configFile ifNotNull {
            runCatching {
                val configurationInfo = fromJson<LoggerConfigStringConverter.ConfigurationInfo>()
                configurationInfo.loggers.forEach { (name, loggerInfo) ->
                    val level = loggerInfo.level?.logLevel()
                    val publishers = loggerInfo.publishers?.mapNotNull { pubId ->
                        configurationInfo.publishers[pubId]?.toPublisher()
                    }?.toMutableList()
                    LoggerStorage.addLoggerInfo(name, level, publishers, loggerInfo.additivity)
                }
            }.onSuccess {
                info { "Successfully loading logger configuration file '$this'" }
            }.onFailure { error ->
                info { "Can't parse logger configuration file '$this' due to $error" }
            }.getOrNull()
        } either {
            info { "Logger config file not specified ($ENV_CONF_PATH empty), default settings will be used" }
        }
    }
}