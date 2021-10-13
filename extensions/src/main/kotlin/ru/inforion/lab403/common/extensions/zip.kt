@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.File
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

inline fun File.toZipFile() = ZipFile(this)

inline fun ZipOutputStream.writeEntry(name: String, action: ZipOutputStream.() -> Unit) {
    val entry = ZipEntry(name)
    putNextEntry(entry)
    action()
    flush()
    closeEntry()
}

inline fun ZipOutputStream.writeEntry(name: String, bytes: ByteArray) = writeEntry(name) { write(bytes) }

inline fun <T> ZipFile.readEntry(name: String, action: InputStream.() -> T): T {
    val entry = requireNotNull(getEntry(name)) { "Entry '$name' not found in ZIP" }
    return getInputStream(entry).use(action)
}

inline fun ZipFile.readEntry(name: String): ByteArray = readEntry(name) { readAllBytes() }

inline fun ZipFile.isFileExists(filename: String) = getEntry(filename) != null

inline fun ZipInputStream.entriesSequence() = sequence {
    var entry = nextEntry
    while (entry != null) {
        yield(entry)
        entry = nextEntry
    }
}

infix fun ZipInputStream.findZipEntry(name: String) = entriesSequence().find { it.name == name }

infix fun ZipInputStream.firstZipEntry(name: String) = findZipEntry(name)
    ?: throw NoSuchElementException("Entry with name $name not found in ZIP-stream")

inline fun <T> ZipInputStream.readEntryOrNull(name: String, action: ZipInputStream.() -> T): T? {
    findZipEntry(name) ?: return null
    return action()
}

inline fun <T> ZipInputStream.readEntry(name: String, action: ZipInputStream.() -> T): T {
    firstZipEntry(name)
    return action()
}