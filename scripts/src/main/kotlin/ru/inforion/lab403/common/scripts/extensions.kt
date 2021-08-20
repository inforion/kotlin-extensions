package ru.inforion.lab403.common.scripts

import org.python.core.PyClass
import org.python.core.PyException

val PyException.pyClass get(): PyClass = type as PyClass

val PyException.pyClassOrNull get(): PyClass? = type as? PyClass