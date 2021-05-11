package ru.inforion.lab403.common.extensions

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream


fun ObjectMapper.enableTyping() {
    // Java json serializer read long as int if:
    //  - it defined as value: Long but it shorter then long after serialization
    enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.EXISTING_PROPERTY)
}


fun jsonParser(
    comments: Boolean = true,
    indent: Boolean = true,
    typing: Boolean = false,
    trailingComma: Boolean = true
) = ObjectMapper().apply {
    configure(JsonParser.Feature.ALLOW_COMMENTS, comments)
    configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, trailingComma)
    configure(SerializationFeature.INDENT_OUTPUT, indent)
    registerModule(JodaModule())
    registerKotlinModule()
    if (typing) enableTyping()
}


// https://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
// using several mappers and get at each time random mapper to prevent heavy deadlocks
val mappers = Array(16) { jsonParser() }


inline fun <reified T: Any> String.parseJson(m: ObjectMapper = mappers.random()) = m.readValue<T>(this)

inline fun <reified T: Any> InputStream.parseJson(m: ObjectMapper = mappers.random()) = m.readValue<T>(this)

inline fun <reified T: Any> Map<String, Any?>.parseJson(m: ObjectMapper = mappers.random()): T = m.convertValue(this, T::class.java)

inline fun <reified T: Any> Any.parseJsonAsMap(m: ObjectMapper = mappers.random()): T = (this as Map<String, Any?>).parseJson(m)

inline fun <reified T: Any> Any.parseJsonAsString(m: ObjectMapper = mappers.random()): T = (this as String).parseJson(m)

inline fun <reified T: Any> T.writeJson(m: ObjectMapper = mappers.random()) = m.writeValueAsString(this)