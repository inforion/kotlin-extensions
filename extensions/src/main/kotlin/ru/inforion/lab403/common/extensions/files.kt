package ru.inforion.lab403.common.extensions

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.file.Paths
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

fun <T : Any> T.getJarDirectory(): String = File(this::class.java.protectionDomain.codeSource.location.toURI().path).path

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

/**
 * {EN}
 * Checks if a path specified as string [this] traverse [base] directory
 *
 * see https://portswigger.net/web-security/file-path-traversal
 *
 * @param base base directory path
 *
 * @return true if [this] path traverse [base] path
 * {EN}
 */
fun String.isNotTraverseDirectory(base: String): Boolean {
    val baseCanonicalPath = base.toFile().canonicalPath
    val childCanonicalPath = base.toFile(this).canonicalPath
    return childCanonicalPath.startsWith(baseCanonicalPath)
}

/**
 * {EN}see [isNotTraverseDirectory]{EN}
 */
fun String.isTraverseDirectory(base: String) = !isNotTraverseDirectory(base)

/**
 * {EN}
 * Checks if a string is a valid path.
 *
 * Calling examples:
 *    isValidPath("c:/test");      //returns true
 *    isValidPath("c:/te:t");      //returns false
 *    isValidPath("c:/te?t");      //returns false
 *    isValidPath("c/te*t");       //returns false
 *    isValidPath("good.txt");     //returns true
 *    isValidPath("not|good.txt"); //returns false
 *    isValidPath("not:good.txt"); //returns false
 *
 * see https://stackoverflow.com/questions/468789/is-there-a-way-in-java-to-determine-if-a-path-is-valid-without-attempting-to-cre
 * {EN}
 */
fun String.isValidPath() = runCatching { Paths.get(this) }.isSuccess