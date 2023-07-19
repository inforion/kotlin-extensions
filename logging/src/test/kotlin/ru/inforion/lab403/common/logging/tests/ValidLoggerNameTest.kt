package ru.inforion.lab403.common.logging.tests

import org.junit.Test
import ru.inforion.lab403.common.logging.logger.Logger
import kotlin.test.assertFails

class ValidLoggerNameTest {
    @Test
    fun testInvalidLoggerName() {
        val invalidChars = "][{}()+*?^\$\\|"
        for(char in invalidChars) {
            val assertionError = assertFails {  Logger.create("Logger$char", false) }
            assert(assertionError.message!!.contains("special characters"))
        }
    }
}