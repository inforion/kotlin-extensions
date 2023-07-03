package ru.inforion.lab403.common.extensions

import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.util.*
import java.util.zip.GZIPInputStream

fun <T: Any, R: Any> getResourceOf(obj: T, resource: String, getter: (Class<T>) -> R?): R {
    val cls: Class<T> = obj.javaClass
    val result = getter(cls)
    if (result == null) {
        val basepath = cls.getResource("")
        throw FileNotFoundException("Can't open resource $resource with path $basepath of $cls")
    }
    return result
}

fun <T: Any> getResourceAsStreamOf(obj: T, resource: String): InputStream =
    getResourceOf(obj, resource) { it.getResourceAsStream(resource) }

fun <T: Any>getGZIPResourceAsStreamOf(obj: T, resource: String) =
        GZIPInputStream(getResourceAsStreamOf(obj, resource))

fun <T: Any> T.getResourceAsStream(resource: String) = getResourceAsStreamOf(this, resource)

fun <T: Any> T.getGZIPResourceAsStream(resource: String) = getGZIPResourceAsStreamOf(this, resource)

fun <T: Any>getResourceUrlOf(obj: T, resource: String): URL =
    getResourceOf(obj, resource) { it.getResource(resource) }

fun <T: Any> T.getResourceUrl(resource: String): URL = getResourceUrlOf(this, resource)

fun <T: Any> getResourceTextOf(obj: T, resource: String) = getResourceUrlOf(obj, resource).readText()

fun <T: Any> T.getResourceText(resource: String) = getResourceUrl(resource).readText()

private fun getClassLoader() = Thread.currentThread().contextClassLoader

fun <R: Any> getResourceOfRoot(resource: String, getter: (ClassLoader) -> R?): R {
    val cl = getClassLoader()
    val result = getter(cl)
    if (result == null) {
        val basepath = cl.getResource("")
        throw FileNotFoundException("Can't open resource $resource with path $basepath")
    }
    return result
}

fun getResourceAsStreamOfRoot(resource: String): InputStream = getResourceOfRoot(resource) { it.getResourceAsStream(resource) }

fun getResourceUrlOfRoot(resource: String): URL = getResourceOfRoot(resource) { it.getResource(resource) }

fun <T: Any> T.buildInformationString(versionProperties: String = "version.properties"): String {
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