package ru.inforion.lab403.common.extensions

import java.io.File
import java.security.MessageDigest
import java.util.*

fun ByteArray.hash(name: String) = MessageDigest.getInstance(name).digest(this)

fun ByteArray.sha1(): ByteArray = hash("SHA1")

fun ByteArray.sha256(): ByteArray = hash("SHA-256")

fun ByteArray.sha512(): ByteArray = hash("SHA-512")

fun ByteArray.md5(): ByteArray = hash("MD5")

fun ByteArray.b64encode(): String = Base64.getEncoder().encodeToString(this)

fun String.b64decode(): ByteArray = Base64.getDecoder().decode(this)

fun File.sha1() = readBytes().sha1()

fun File.sha256() = readBytes().sha256()

fun File.sha512() = readBytes().sha512()

fun File.md5() = readBytes().md5()