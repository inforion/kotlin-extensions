@file:Suppress("unused")

package ru.inforion.lab403.common.json

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.ConstructorDetector.USE_PROPERTIES_BASED
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
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
//    setConstructorDetector(USE_PROPERTIES_BASED)
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

inline fun <reified T: Any> String.fromJson(m: ObjectMapper = mappers.random()) = m.readValue<T>(this)

inline fun <reified T: Any> InputStream.fromJson(m: ObjectMapper = mappers.random()) = m.readValue<T>(this)

inline fun <reified T: Any> File.fromJson(m: ObjectMapper = mappers.random()) = m.readValue<T>(this)

inline fun <reified T: Any> Map<String, Any?>.fromJson(m: ObjectMapper = mappers.random()): T = m.convertValue(this, T::class.java)

inline fun <reified T: Any> File.toJson(data: T, m: ObjectMapper = mappers.random()) = m.writeValue(this, data)

inline fun <reified T: Any> T.toJson(m: ObjectMapper = mappers.random()): String = m.writeValueAsString(this)