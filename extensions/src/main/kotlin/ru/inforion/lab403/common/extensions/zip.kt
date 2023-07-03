@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.File
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

fun File.toZipFile() = ZipFile(this)

fun ZipOutputStream.writeEntry(name: String, action: ZipOutputStream.() -> Unit) {
    val entry = ZipEntry(name)
    putNextEntry(entry)
    action()
    flush()
    closeEntry()
}

fun ZipOutputStream.writeEntry(name: String, bytes: ByteArray) = writeEntry(name) { write(bytes) }

fun <T> ZipFile.readEntry(name: String, action: InputStream.() -> T): T {
    val entry = requireNotNull(getEntry(name)) { "Entry '$name' not found in ZIP" }
    return getInputStream(entry).use(action)
}

fun ZipFile.readEntry(name: String): ByteArray = readEntry(name) { readAllBytes() }

fun ZipFile.isFileExists(filename: String) = getEntry(filename) != null

fun ZipInputStream.entriesSequence() = sequence {
    var entry = nextEntry
    while (entry != null) {
        yield(entry)
        entry = nextEntry
    }
}

fun ZipInputStream.findZipEntry(name: String) = entriesSequence().find { it.name == name }

fun ZipInputStream.firstZipEntry(name: String) = findZipEntry(name)
    ?: throw NoSuchElementException("Entry with name $name not found in ZIP-stream")

fun <T> ZipInputStream.readEntryOrNull(name: String, action: ZipInputStream.() -> T): T? {
    findZipEntry(name) ?: return null
    return action()
}

fun <T> ZipInputStream.readEntry(name: String, action: ZipInputStream.() -> T): T {
    firstZipEntry(name)
    return action()
}