package ru.inforion.lab403.common.extensions

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.net.URI
import java.net.URL
import java.util.zip.GZIPInputStream

fun createTimeFile(prefix: String? = null, suffix: String? = null, directory: File? = null): File {
    val realPrefix = prefix ?: System.currentTimeMillis().toString()
    return File.createTempFile(realPrefix, suffix, directory).also { it.deleteOnExit() }
}

@Deprecated("use operator div()")
fun joinPaths(vararg paths: String): String {
    if (paths.isEmpty())
        return ""

    var result = File(paths[0])
    for (k in 1 until paths.size)
        result = File(result.path, paths[k])

    return result.path
}

fun gzipInputStreamIfPossible(path: String): InputStream {
    var result: InputStream = FileInputStream(path)
    if (path.endsWith(".gz"))
        result = GZIPInputStream(result)
    return result
}

fun <T: Any> T.getJarDirectory(): String = File(this::class.java.protectionDomain.codeSource.location.toURI().path).path

fun walkTo(result: MutableCollection<File>, folder: File, depth: Int): MutableCollection<File> {
    if (!folder.isDirectory)
        return result

    if (depth == -1)
        return result

    folder.listFiles().forEach { entry ->
        result.add(entry)
        walkTo(result, entry, depth - 1)
    }

    return result
}

fun walk(folder: File, depth: Int = Int.MAX_VALUE) = walkTo(arrayListOf(), folder, depth)

fun File?.getInternalFileURL(path: String, type: String = "jar"): URL {
    if (this == null)
        return File(path).toURI().toURL()

    if (File(path).isAbsolute)
        throw IllegalArgumentException("Path $path must not be absolute!")

    return URI("$type:file:${toURI().path}!/$path").toURL()
}

/**
 * {EN}
 * Operator function make possible to join paths using / operation between [File] and [String]
 * val base = File("base")
 * val fullpath = base / "test"
 *
 * @param child path suffix
 * @return joined paths
 * {EN}
 */
operator fun File.div(child: String): File = File(this, child)

/**
 * {EN}
 * Operator function make possible to join paths using / operation between [File] and [File]
 * val base = File("base")
 * val test = File("test")
 * val fullpath = base / test
 *
 * @param child path suffix
 * @return joined paths
 * {EN}
 */
operator fun File.div(child: File): File = this / child.path

/**
 * {EN}
 * Property function to simplify get of the location associated with this CodeSource for class
 *
 * @return the location or raise NPE
 * {EN}
 */
val <T>Class<T>.location get() = protectionDomain.codeSource.location.toURI().path