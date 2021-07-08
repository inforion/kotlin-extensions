@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import java.io.File
import java.security.MessageDigest
import java.util.*

inline fun ByteArray.hash(name: String): ByteArray = MessageDigest.getInstance(name).digest(this)

inline fun ByteArray.sha1(): ByteArray = hash("SHA1")

inline fun ByteArray.sha256(): ByteArray = hash("SHA-256")

inline fun ByteArray.sha512(): ByteArray = hash("SHA-512")

inline fun ByteArray.md5(): ByteArray = hash("MD5")

inline fun String.hash(name: String) = bytes.hash(name)

inline fun String.sha1() = bytes.sha1()

inline fun String.sha256() = bytes.sha256()

inline fun String.sha512() = bytes.sha512()

inline fun String.md5() = bytes.md5()

inline fun ByteArray.b64encode(): String = Base64.getEncoder().encodeToString(this)

inline fun String.b64decode(): ByteArray = Base64.getDecoder().decode(this)

inline fun File.sha1() = readBytes().sha1()

inline fun File.sha256() = readBytes().sha256()

inline fun File.sha512() = readBytes().sha512()

inline fun File.md5() = readBytes().md5()