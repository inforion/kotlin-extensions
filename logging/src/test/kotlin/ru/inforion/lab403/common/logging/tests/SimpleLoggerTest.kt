package ru.inforion.lab403.common.logging.tests

import org.junit.Test
import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.logger


internal class SimpleLoggerTest {
    @Test
    fun test1() {
        val log = logger(FINE)

        log.warning { "First severe message..." }
    }
}