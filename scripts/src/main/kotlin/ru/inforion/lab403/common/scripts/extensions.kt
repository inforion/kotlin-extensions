package ru.inforion.lab403.common.scripts

import org.python.core.*

val PyException.pyClass get(): PyClass = type as PyClass

val PyException.pyClassOrNull get(): PyClass? = type as? PyClass

val PyException.pyType get(): PyType = type as PyType

val PyException.pyJavaType get(): PyJavaType = type as PyJavaType

val PyException.pyObjectDerivedValue get(): PyObjectDerived = value as PyObjectDerived

val PyException.pyBaseExceptionDerivedValue get(): PyBaseExceptionDerived = value as PyBaseExceptionDerived

val PyException.pyValueString get(): PyString = value as PyString

val PyType.pyBase get(): PyJavaType = base as PyJavaType

val PyJavaType.pyBase get(): PyJavaType = base as PyJavaType

// Python Exceptions that inherited from Java Exception encapsulated in proxy PyTypes
val PyException.proxyType get(): Class<*> = pyType.pyBase.pyBase.proxyType