package ru.inforion.lab403.common.logging.formatters

fun stackTraceElementCollector(index: Int): StackTraceElement? =
    Thread.currentThread().stackTrace.getOrNull(index)

fun millisecondsCollector() = System.currentTimeMillis()