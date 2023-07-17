package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.extensions.either
import ru.inforion.lab403.common.extensions.ifNotNull
import ru.inforion.lab403.common.extensions.otherwise
import ru.inforion.lab403.common.json.fromJson
import ru.inforion.lab403.common.logging.LogLevel
import ru.inforion.lab403.common.logging.Messenger
import ru.inforion.lab403.common.logging.logLevel
import ru.inforion.lab403.common.logging.publishers.AbstractPublisher
import java.io.File


object Config {
    const val ENV_CONF_PATH = "INFORION_LOGGING_CONF_PATH"
    const val ENV_DEBUG_ENABLED = "INFORION_LOGGING_PRINT"

    private inline fun <T> info(message: Messenger<T>) = System.err.println(message().toString())

    private inline fun <T> debug(message: Messenger<T>) {
        if (isDebugEnabled) info(message)
    }

    private fun env(name: String): String? = System.getenv(name).also {
        if (it != null) info { "$name: $it" }
    }

    private val isDebugEnabled by lazy {
        env(ENV_DEBUG_ENABLED) ifNotNull { toBoolean() } either { false }
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

    data class PublisherInfo(val cls: String, val args: List<Any>)

    data class LoggerInfo(val level: String? = null, val publishers: List<PublisherInfo>? = null)

    private fun PublisherInfo.toPublisher(): AbstractPublisher {
        val cls = Class.forName(cls)
        val args = args.toTypedArray()
        val types = args.map { arg ->
            when (val type = arg.javaClass) {
                java.lang.Byte::class.java -> Byte::class.java
                java.lang.Short::class.java -> Short::class.java
                java.lang.Integer::class.java -> Int::class.java
                java.lang.Long::class.java -> Long::class.java
                java.lang.Boolean::class.java -> Boolean::class.java
                else -> type
            }
        }.toTypedArray()
        val constructor = cls.getConstructor(*types)
        return constructor.newInstance(*args) as AbstractPublisher
    }

    private val config by lazy {
        configurations ifNotNull {
            runCatching {
                fromJson<Map<String, LoggerInfo>>()
            }.onSuccess {
                info { "Successfully loading logger configuration file '$this'" }
            }.onFailure { error ->
                info { "Can't parse logger configuration file '$this' due to $error" }
            }.getOrNull()
        } either {
            emptyMap()
        }
    }

    private fun LoggerInfo.levelOrNull() = level ifNotNull { logLevel() }

    private fun LoggerInfo.publishersOrNull() = publishers ifNotNull { map { it.toPublisher() }.toTypedArray() }

    /**
     * @since 0.2.4
     */
    fun level(name: String, default: () -> LogLevel): LogLevel =
        config[name]?.levelOrNull()
            ?: config["all"]?.levelOrNull()
            ?: default()

    /**
     * @since 0.4.5
     */
    fun levelOrNull(name: String) = config[name]?.levelOrNull()

    /**
     * @since 0.2.4
     */
    fun publishers(name: String, default: () -> Array<out AbstractPublisher>) =
        config[name]?.publishersOrNull()
            ?: config["all"]?.publishersOrNull()
            ?: default()

    /**
     * @since 0.4.5
     */
    fun publishersOrNull(name: String) = config[name]?.publishersOrNull()
}