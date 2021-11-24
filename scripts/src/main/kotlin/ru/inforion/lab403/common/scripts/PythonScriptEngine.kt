package ru.inforion.lab403.common.scripts

import org.python.core.*
import org.python.jsr223.PyScriptEngine
import org.python.modules.cPickle
import java.lang.reflect.Proxy

class PythonScriptEngine(engine: PyScriptEngine) : AbstractScriptEngine<PyScriptEngine>(engine) {
    companion object {
        init {
            // null pointer exception for dispatch_table otherwise
            cPickle.classDictInit(PyObject(PyType.TYPE))
        }
    }

    override val name = "python"

    override fun serialize(value: Any): ByteArray {
        val dump = cPickle.dumps(value as PyObject)
        return dump.toBytes()
    }

    override fun deserialize(bytes: ByteArray): PyObject {
        val array = PyByteArray(bytes)
        return cPickle.loads(array) as PyObject
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> toInterface(obj: Any, cls: Class<out T>, default: T?): T {
        require(cls.isInterface) { "interface expected" }

//        interp.setLocals(PyScriptEngineScope(this, context))

        val thiz = Py.java2py(obj)

        return Proxy.newProxyInstance(cls.classLoader, arrayOf(cls)) { proxy, method, args ->
//                interp.setLocals(PyScriptEngineScope(this@PyScriptEngine, context))

            val array = args ?: emptyArray()

            val pyMethod = thiz.__findattr__(method.name)

            when {
                pyMethod != null -> {
                    val result = pyMethod.__call__(Py.javas2pys(*array))
                    result.__tojava__(Any::class.java)
                }
                default != null -> method.invoke(default, *array)
                else -> throw NoSuchMethodException(method.name)
            }
        } as T
    }
}