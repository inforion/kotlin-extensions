@file:Suppress("DEPRECATION")

package ru.inforion.lab403.common.logging.tests

import org.junit.Test
import ru.inforion.lab403.common.logging.logger
import java.util.logging.Level


internal class CompatLoggerTest {
    @Test
    fun test1() {
        val log = logger(Level.FINE)

        log.severe { "First severe message..." }
    }
}