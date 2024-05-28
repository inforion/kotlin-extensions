package ru.inforion.lab403.common.logging.logger

import ru.inforion.lab403.common.logging.LogLevel

class Record constructor(
    val logger: Logger,
    val level: LogLevel,
    val millis: Long?,
    val thread: Thread?,
    val stackFrameIndex: Int = -1
)