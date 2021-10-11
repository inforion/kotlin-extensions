package ru.inforion.lab403.common.kryo

import com.esotericsoftware.kryo.*
import com.esotericsoftware.kryo.serializers.FieldSerializer
import com.esotericsoftware.kryo.util.DefaultClassResolver
import com.esotericsoftware.kryo.util.MapReferenceResolver
import com.twitter.chill.ObjectSerializer
import org.objenesis.strategy.InstantiatorStrategy
import org.objenesis.strategy.StdInstantiatorStrategy
import java.lang.reflect.Modifier

class ExtendedKryo(
    classResolver: ClassResolver = DefaultClassResolver(),
    referenceResolver: ReferenceResolver = MapReferenceResolver()
) : Kryo(classResolver, referenceResolver) {

    companion object {
        fun createAndConfigure(cl: ClassLoader? = null) = ExtendedKryo().apply {
            registerDateTime()
            registerByteBuffer()
            registerRanges()
            registerUnsigned()

            instantiatorStrategy = StdInstantiatorStrategy()

            isRegistrationRequired = false
            references = true

            if (cl != null) classLoader = cl
        }
    }

    private val objSer by lazy { ObjectSerializer<Any>() }

    private var strategy: InstantiatorStrategy? = null

    override fun newDefaultSerializer(type: Class<*>): Serializer<*> {
        if (isSingleton(type)) return objSer
        val serializer = super.newDefaultSerializer(type)
        if (serializer is FieldSerializer) serializer.setIgnoreSyntheticFields(false)
        return serializer
    }

    private fun isSingleton(klass: Class<*>): Boolean = klass.name.last() == '$' && objSer.accepts(klass)

    private fun tryStrategy(cls: Class<*>): InstantiatorStrategy = strategy ?: run {
        val message = if (cls.isMemberClass && !Modifier.isStatic(cls.modifiers))
            "Class cannot be created (non-static member class): ${cls.name}"
        else
            "Class cannot be created (missing no-arg constructor): ${cls.name}"

        throw KryoException(message)
    }

    override fun setInstantiatorStrategy(st: InstantiatorStrategy) {
        super.setInstantiatorStrategy(st)
        strategy = st
    }

    override fun newInstantiator(cls: Class<*>) = newTypedInstantiator(cls)

    private fun <T> newTypedInstantiator(cls: Class<T>) = Instantiators.newOrElse(
        cls,
        listOf(
            { Instantiators.reflectAsm(it) },
            { Instantiators.normalJava(it) }
        ),
        tryStrategy(cls).newInstantiatorOf(cls)
    )
}