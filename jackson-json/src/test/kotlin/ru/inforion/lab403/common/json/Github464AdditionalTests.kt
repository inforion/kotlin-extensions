package ru.inforion.lab403.common.json

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test

class Github464AdditionalTests {
    interface IValue

    @JvmInline
    value class ValueClass(val value: Int) : IValue

    abstract class AbstractGetter<T> {
        abstract val byAbstractGenericVal: T

        fun <T> getByAbstractGenericFun() = byAbstractGenericVal
    }
    interface IGetter<T> {
        val byInterfaceGenericVal: T

        fun <T> getByInterfaceGenericDefaultFun() = byInterfaceGenericVal
    }

    @JsonPropertyOrder(alphabetic = true)
    data class BoxedFields(
        val asInterface: IValue,
        override val byAbstractGenericVal: ValueClass,
        override val byInterfaceGenericVal: ValueClass,
        val nullableValue: ValueClass?
    ) : AbstractGetter<ValueClass>(), IGetter<ValueClass> {
        constructor(value: ValueClass) : this(value, value, value, value)
    }

    @Test
    fun test() {
        val target = BoxedFields(ValueClass(1))
        val mapper: ObjectMapper = jsonParser()

        val string = mapper.writeValueAsString(target)

        println(string)

        val actual = mapper.readValue<BoxedFields>(string)

        println(actual)
    }
}