package ru.inforion.lab403.common.logging.config

import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.extensions.otherwise
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.logging.Levels
import ru.inforion.lab403.common.logging.Messenger
import java.io.File

class LoggerFileConfigInitializer : ILoggerConfigInitializer {
    companion object {
        const val ENV_CONF_PATH = "INFORION_LOGGING_CONF_PATH"
        const val ENV_DEBUG_ENABLED = "INFORION_LOGGING_PRINT"
    }

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

    private val configFile: File? by lazy {
        val path = env(ENV_CONF_PATH)

        if (path != null) {
            File(path).takeIf { it.isFile } otherwise {
                info { "Logging configuration file can't be loaded: $this" }
            }
        } else {
            debug { "$ENV_CONF_PATH not specified" }; null
        }
    }

    override fun load() {
        configFile ifNotNull {
            runCatching {
                fromJson<Map<String, LoggerConfigStringConverter.LoggerInfo>>().forEach { (name, loggerInfo) ->

                    // TODO: если какой-то один инициализатор (changeLevel или addPublisher) сломался, что лучше?
                    // 1. Откатить все остальные
                    // 2. Оставить то, что удалось инициализиоровать и свалиться в ошибку
                    // 3. Пропустить ошибочный инициализатор и продолжить?

                    if (loggerInfo.level != null)
                        LoggerConfig.changeLevel(Levels.valueOf(loggerInfo.level).level, name)

                    if (loggerInfo.publishers != null)
                        for (publisher in loggerInfo.publishers)
                            LoggerConfig.addPublisher(publisher.toPublisher(), name)
                }
            }.onSuccess {
                info { "Successfully loading logger configuration file '$this'" }
            }.onFailure { error ->
                info { "Can't parse logger configuration file '$this' due to $error" }
            }.getOrNull()
        } either {
            info { "Logger config file not found, default settings will be used" }
        }
    }
}