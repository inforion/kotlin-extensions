package ru.inforion.lab403.common.logging.publishers

import ru.inforion.lab403.common.logging.logger.Record
import java.io.StringWriter
import java.io.Writer


class TestMockWriter : Writer() {
    var string = StringWriter()
        private set

    var isClosed = false
        private set

    override fun close() {
        if (isClosed) {
            throw IllegalStateException("TestMockWriter <$this> already closed")
        }
        isClosed = true
    }

    override fun flush() {
        if (isClosed) {
            throw IllegalStateException("TestMockWriter <$this> s closed")
        }
        string.flush()
    }

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        if (isClosed) {
            throw IllegalStateException("TestMockWriter <$this> s closed")
        }

        string.write(cbuf, off, len)
    }

    fun getAndClear() = string.toString().also {
        string = StringWriter()
    }
}
