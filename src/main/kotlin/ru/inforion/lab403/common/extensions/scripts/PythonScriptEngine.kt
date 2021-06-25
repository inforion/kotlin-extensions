package ru.inforion.lab403.common.extensions.scripts

import org.python.core.PyByteArray
import org.python.core.PyObject
import org.python.jsr223.PyScriptEngine
import org.python.modules.cPickle
import ru.inforion.lab403.common.extensions.scripts.AbstractScriptEngine

class PythonScriptEngine(engine: PyScriptEngine) : AbstractScriptEngine<PyScriptEngine>(engine) {
    override val name = "python"

    override fun deserialize(bytes: ByteArray): PyObject {
        val array = PyByteArray(bytes)
        return cPickle.loads(array) as PyObject
    }
}