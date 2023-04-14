package ru.inforion.lab403.common.logging.tests

import org.junit.Test
import ru.inforion.lab403.common.logging.logger.Slf4jLoggerImpl
import kotlin.test.assertEquals

class FormatLoggerTest {
    @Test
    fun testFormatString() {
        val log = Slf4jLoggerImpl("Logger")

        log.warn("message {} with {} args: {}", 1, "different", 3)
        assertEquals(
            log.formatString("message {} with {} args: {}", 1, "different", 3),
            "message 1 with different args: 3"
        )
    }
}
