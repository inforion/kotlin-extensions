package ru.inforion.lab403.common.extensions

import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*
import java.util.zip.GZIPInputStream

/**
 * Created by Alexei Gladkikh on 29/01/17.
 */
inline fun <reified T: Any>getResourceAsStreamOf(obj: T, resource: String): InputStream {
    val cls: Class<T> = obj.javaClass
    val stream = cls.getResourceAsStream(resource)
    if (stream == null) {
        val basepath = cls.getResource("")
        throw FileNotFoundException("Can't open resource $resource with path $basepath class $cls")
    }
    return stream
}

inline fun <reified T: Any>getGZIPResourceAsStreamOf(obj: T, resource: String): InputStream =
        GZIPInputStream(getResourceAsStreamOf(obj, resource))

inline fun <reified T: Any> T.getResourceAsStream(resource: String): InputStream = getResourceAsStreamOf(this, resource)
inline fun <reified T: Any> T.getGZIPResourceAsStream(resource: String): InputStream = getGZIPResourceAsStreamOf(this, resource)

inline fun <reified T: Any> T.buildInformationString(versionProperties: String = "version.properties"): String {
    val result: String
    val prop = Properties()
    val stream = getResourceAsStream(versionProperties)
    with(stream) { prop.load(this) }

    val initialized = prop.getProperty("initialized")
    if (initialized != "\$initialized") {
        val name = prop.getProperty("name")
        val version = prop.getProperty("version")
        val revision = prop.getProperty("revision")
        val timestamp = prop.getProperty("timestamp")
        val build = prop.getProperty("build")
        result = "$name-$version-$revision-$timestamp-$build"
    } else {
        result = "version information not available"
    }

    return result
}