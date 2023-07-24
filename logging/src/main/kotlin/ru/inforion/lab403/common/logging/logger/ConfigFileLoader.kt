package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.extensions.otherwise
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.logging.Levels
import ru.inforion.lab403.common.logging.Messenger
import ru.inforion.lab403.common.logging.logger.Config.toPublisher
import java.io.File

class ConfigFileLoader : IConfigFileLoader {
    private val ENV_CONF_PATH = "INFORION_LOGGING_CONF_PATH"
    private val ENV_DEBUG_ENABLED = "INFORION_LOGGING_PRINT"
    private val DEFAULT_LEVEL = "INFO"

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
                fromJson<Map<String, Config.LoggerInfo>>().forEach { name, loggerInfo ->
                    if (loggerInfo.level != null)
                        Config.changeLevel(Levels.valueOf(loggerInfo.level).level, name)

                    if (loggerInfo.publishers != null)
                        for (publisher in loggerInfo.publishers)
                            Config.addPublisher(publisher.toPublisher(), name)
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