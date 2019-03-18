package ru.inforion.lab403.common.extensions

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.sql.*
import java.util.*
import java.util.Date

/**
 * Created by Alexei Gladkikh on 23/01/17.
 */

fun <T>ResultSet.yieldBy(generate: (r: ResultSet) -> T): Iterable<T> = object : Iterable <T> {
    override operator fun iterator(): Iterator<T> = object : Iterator<T> {
        override fun next(): T = generate(this@yieldBy)

        override fun hasNext(): Boolean {
            val next = this@yieldBy.next()
            if (!next) this@yieldBy.close()
            return next
        }
    }
}


@Suppress("UNCHECKED_CAST")
fun <T> ResultSet.getAnyResultByField(i: Int, field: Field): T {
    try {
        return when (field.type) {
            Int::class.java -> getInt(i) as T
            Long::class.java -> getLong(i) as T
            String::class.java -> getString(i) as T
            Boolean::class.java -> getBoolean(i) as T
            Timestamp::class.java -> getTimestamp(i) as T
            Byte::class.java -> getByte(i) as T
            Short::class.java -> getShort(i) as T
            Float::class.java -> getFloat(i) as T
            Double::class.java -> getDouble(i) as T
            Date::class.java -> getDate(i) as T
            Time::class.java -> getTime(i) as T
            ByteArray::class.java -> getBytes(i) as T
            else -> throw UnsupportedOperationException("Unsupported class: ${field.type.simpleName}")
        }
    } catch (error: SQLException) {
        throw error
    }
}

val constructors = HashMap<Class<*>, Constructor<*>>()

inline fun <reified T: Any> ResultSet.getRecord(): T {
    val fields = T::class.java.declaredFields
    var constructor = constructors[T::class.java]
    if (constructor == null) {
        val klass = T::class.java
        val types = Array(fields.size) { fields[it].type }
        constructor = klass.getConstructor(*types)
        constructors[T::class.java] = constructor
    }
    val args = Array<Any>(metaData.columnCount) { getAnyResultByField(it + 1, fields[it]) }
    @Suppress("UNCHECKED_CAST")
    return (constructor as Constructor<T>).newInstance(*args)
}

inline fun <T>ResultSet.extractByOrDefault(getter: (r: ResultSet) -> T, default: () -> T): T {
    this.next()
    try {
        val value = getter(this)
        return value
    } catch (e: SQLException) {
        return default()
    } finally {
        this.close()
    }
}

inline fun <T>ResultSet.extractBy(getter: (r: ResultSet) -> T): T {
    this.next()
    val value = getter(this)
    this.close()
    return value
}

inline fun <T>ResultSet.extractByOrNull(getter: (r: ResultSet) -> T): T? {
    this.next()
    try {
        val value = getter(this)
        return value
    } catch (e: SQLException) {
        return null
    } finally {
        this.close()
    }
}

fun ResultSet.extractOrDefault(default: ResultSet): ResultSet = extractByOrDefault({ it }, { default })
fun ResultSet.extractStringOrDefault(default: String): String = extractByOrDefault({ it.getString(1) }, { default })
fun ResultSet.extractLongOrDefault(default: Long): Long = extractByOrDefault({ it.getLong(1) }, { default })
fun ResultSet.extractIntOrDefault(default: Int): Int = extractByOrDefault({ it.getInt(1) }, { default })
fun ResultSet.extractShortOrDefault(default: Short): Short = extractByOrDefault({ it.getShort(1) }, { default })
fun ResultSet.extractByteOrDefault(default: Byte): Byte = extractByOrDefault({ it.getByte(1) }, { default })
fun ResultSet.extractBooleanOrDefault(default: Boolean): Boolean = extractByOrDefault({ it.getBoolean(1) }, { default })

fun ResultSet.extractString(): String = extractBy { it.getString(1) }
fun ResultSet.extractLong(): Long = extractBy { it.getLong(1) }
fun ResultSet.extractInt(): Int = extractBy { it.getInt(1) }
fun ResultSet.extractShort(): Short = extractBy { it.getShort(1) }
fun ResultSet.extractByte(): Byte = extractBy { it.getByte(1) }
fun ResultSet.extractBytes(): ByteArray = extractBy { it.getBytes(1) }
fun ResultSet.extractBoolean(): Boolean = extractBy { it.getBoolean(1) }
fun ResultSet.extractTimestamp(): Timestamp = extractBy { it.getTimestamp(1) }
inline fun <reified T: Any>ResultSet.extractRecord(): T = extractBy { it.getRecord<T>() }


fun Connection.withoutAutoCommit(block: () -> Unit) {
    val autoCommit = this.autoCommit
    this.autoCommit = false
    try {
        block()
    } finally {
        this.autoCommit = autoCommit
    }
}


fun Connection.withAutoCommit(block: () -> Unit) {
    val autoCommit = this.autoCommit
    this.autoCommit = true
    try {
        block()
    } finally {
        this.autoCommit = autoCommit
    }
}

fun PreparedStatement.setAny(i: Int, arg: Any) {
    when (arg) {
        is Int -> setInt(i, arg)
        is Short -> setShort(i, arg)
        is Long -> setLong(i, arg)
        is Boolean -> setBoolean(i, arg)
        is String -> setString(i, arg)
        is Timestamp -> setTimestamp(i, arg)
        is ByteArray -> setBytes(i, arg)
        is Float -> setFloat(i, arg)
        is Double -> setDouble(i, arg)
        else -> throw UnsupportedOperationException("Unsupported: $arg")
    }
}