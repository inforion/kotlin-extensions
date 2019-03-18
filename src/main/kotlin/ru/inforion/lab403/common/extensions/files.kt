package ru.inforion.lab403.common.extensions

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.net.URI
import java.net.URL
import java.util.zip.GZIPInputStream

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