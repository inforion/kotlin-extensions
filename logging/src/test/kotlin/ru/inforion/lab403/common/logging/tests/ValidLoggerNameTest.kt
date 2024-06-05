package ru.inforion.lab403.common.logging.tests

import org.junit.jupiter.api.Test
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.WARNING
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.storage.LoggerStorage
import kotlin.test.assertEquals

class ValidLoggerNameTest {
//    @Test
//    fun testInvalidLoggerName() {
//        val invalidChars = "][{}()+*?^\$\\|"
//        for(char in invalidChars) {
//            val assertionError = assertFails {  Logger.create("Logger$char") }
//            assert(assertionError.message!!.contains("special characters"))
//        }
//    }

    @Test
    fun testNameStartWithoutPoint() {
        val log = logger(INFO)
        assertEquals(INFO, LoggerStorage.getLevel(".ru.inforion.lab403.common.logging.tests.ValidLoggerNameTest"))
        assertEquals(INFO, LoggerStorage.getLevel("ru.inforion.lab403.common.logging.tests.ValidLoggerNameTest"))
        LoggerStorage.setLevel("ru.inforion.lab403.common.logging.tests.ValidLoggerNameTest", WARNING)
        assertEquals(WARNING, LoggerStorage.getLevel(".ru.inforion.lab403.common.logging.tests.ValidLoggerNameTest"))
        LoggerStorage.setLevel(LoggerStorage.ALL, WARNING)
        assertEquals(WARNING, LoggerStorage.getLevel(LoggerStorage.ALL))
    }
}