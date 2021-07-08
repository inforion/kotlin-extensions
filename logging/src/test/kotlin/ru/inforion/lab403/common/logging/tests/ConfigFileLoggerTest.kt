@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import org.junit.Test
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.logger

internal class ConfigFileLoggerTest {
    @Test
    fun test1() {
        withEnvironmentVariable("INFORION_LOGGING_CONF_PATH", "src/test/resources/config.json")
            .and("INFORION_LOGGING_PRINT", "true")
            .execute {
                val log = logger(INFO)
                log.fine { "Fine message for INFO logger reconfigured by config file" }
                log.severe { "First severe message..." }
            }
    }
}