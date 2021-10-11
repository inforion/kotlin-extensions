@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

inline fun ZipOutputStream.writeEntry(name: String, action: ZipOutputStream.() -> Unit) {
    val entry = ZipEntry(name)
    putNextEntry(entry)
    action()
    flush()
    closeEntry()
}

inline fun <T> ZipFile.readEntry(name: String, action: InputStream.() -> T): T {
    val entry = requireNotNull(getEntry(name)) { "Entry '$name' not found in ZIP" }
    return getInputStream(entry).use(action)
}

inline fun ZipFile.isFileExists(filename: String) = getEntry(filename) != null