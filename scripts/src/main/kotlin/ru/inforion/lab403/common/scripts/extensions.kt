package ru.inforion.lab403.common.scripts

import org.python.core.*

val PyException.pyClass get(): PyClass = type as PyClass

val PyException.pyClassOrNull get(): PyClass? = type as? PyClass

val PyException.pyType get(): PyType = type as PyType

val PyException.pyValue get(): PyBaseExceptionDerived = value as PyBaseExceptionDerived