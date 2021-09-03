package ru.inforion.lab403.common.utils

import ru.inforion.lab403.common.logging.FINE
import ru.inforion.lab403.common.logging.logger
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class Shell(vararg val cmd: String, val timeout: Long = -1) {
    companion object {
        val log = logger(FINE)
    }

    var status: Int = 0
    var stdout = String()
    var stderr = String()

    fun execute(): Shell {
        val bout = ByteArrayOutputStream()
        val berr = ByteArrayOutputStream()

        log.finer { "Executing shell command: ${cmd.joinToString(" ")}" }
        val process = Runtime.getRuntime().exec(cmd)

        process.inputStream.copyTo(bout)
        process.errorStream.copyTo(berr)

        if (timeout == -1L)
            process.waitFor()
        else
            process.waitFor(timeout, TimeUnit.MILLISECONDS)

        status = process.exitValue()
        stdout = bout.toString()
        stderr = berr.toString()
        return this
    }
}