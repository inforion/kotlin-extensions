@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.extensions

import java.io.File
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import kotlin.experimental.and

operator fun String.get(range: IntRange): String {
    val tmp = if (range.last < 0) range.first..length + range.last else range
    return slice(tmp.first until tmp.last)
}

fun String.toCamelCase(): String {
    if (isNotEmpty())
        return this[0].lowercaseChar() + substring(1)
    return this
}

fun ByteArray.ascii(): String = map { if (it in 0x20..0x7e) it.char else '.' }.joinToString("")

fun ByteArray.hexlify(upperCase: Boolean = true, separator: Char? = null): String {
    val hexChars = CharArray(this.size * 2)
    for (j in 0 until this.size) {
        val v = this[j]
        hexChars[j * 2] = Character.forDigit(v[7..4], 16)
        hexChars[j * 2 + 1] = Character.forDigit(v[3..0], 16)
    }
    val result = if (upperCase) String(hexChars).uppercase() else String(hexChars)
    if (separator != null) {
        val builder = StringBuilder()
        builder.append(result[0..2])
        for (i in 2 until result.length step 2) {
            builder.append(separator)
            builder.append(result[i..i + 2])
        }
        return builder.toString()
    }
    return result
}

fun String.unhexlify(): ByteArray {
    val tmp = this.replace(" ", "")
    val data = ByteArray(tmp.length / 2)
    for (k in 0 until tmp.length step 2) {
        val ch0 = Character.digit(tmp[k + 0], 16)
        val ch1 = Character.digit(tmp[k + 1], 16)
        data[k / 2] = (ch0 shl 4 or ch1).byte
    }
    return data
}

inline fun ULong.binary(n: Int): String = buildString { (n - 1 downTo 0).forEach { append(this[it]) } }
inline fun UInt.binary(n: Int): String = buildString { (n - 1 downTo 0).forEach { append(this[it]) } }
inline fun UShort.binary(n: Int): String = uint_z.binary(n)
inline fun UByte.binary(n: Int): String = uint_z.binary(n)

inline val ULong.binary: String get() = binary(64)
inline val UInt.binary: String get() = binary(32)
inline val UShort.binary: String get() = binary(16)
inline val UByte.binary: String get() = binary(8)

inline val Long.binary: String get() = ulong.binary
inline val Int.binary: String get() = uint.binary
inline val Short.binary: String get() = ushort.binary
inline val Byte.binary: String get() = ubyte.binary

fun ByteArray.decode(charset: Charset = Charsets.UTF_8): String {
    var size = 0
    while (this[size].int_z != 0) size++
    return String(this, 0, size, charset)
}

fun String.toUnhexlifyByteBuffer(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteBuffer =
    ByteBuffer.wrap(this.unhexlify()).apply { order(byteOrder) }

inline fun String.long(radix: Int = 10): Long = toLong(radix)
inline fun String.int(radix: Int = 10): Int = toInt(radix)
inline fun String.short(radix: Int = 10): Short = toShort(radix)
inline fun String.byte(radix: Int = 10): Byte = toByte(radix)

inline fun String.ulong(radix: Int = 10): ULong = java.lang.Long.parseUnsignedLong(this, radix).ulong
inline fun String.uint(radix: Int = 10): UInt = Integer.parseUnsignedInt(this, radix).uint

inline val String.longByDec get() = long(10)
inline val String.intByDec get() = int(10)
inline val String.shortByDec get() = short(10)
inline val String.byteByDec get() = byte(10)
inline val String.ulongByDec get() = ulong(10)
inline val String.uintByDec get() = uint(10)

inline val String.longByHex get() = long(16)
inline val String.intByHex get() = int(16)
inline val String.shortByHex get() = short(16)
inline val String.byteByHex get() = byte(16)
inline val String.ulongByHex get() = ulong(16)
inline val String.uintByHex get() = uint(16)

inline val String.long: Long get() = removePrefixOrNull("0x")?.longByHex ?: longByDec
inline val String.int: Int get() = removePrefixOrNull("0x")?.intByHex ?: intByDec
inline val String.short: Short get() = removePrefixOrNull("0x")?.shortByHex ?: shortByDec
inline val String.byte: Byte get() = removePrefixOrNull("0x")?.byteByHex ?: byteByDec

inline val String.ulong: ULong get() = removePrefixOrNull("0x")?.ulongByHex ?: ulongByDec
inline val String.uint: UInt get() = removePrefixOrNull("0x")?.uintByHex ?: uintByDec

inline val String.bool: Boolean get() = toBoolean()
inline val String.double: Double get() = toDouble()
inline val String.float: Float get() = toFloat()


infix fun String.resembles(other: String) = asSequence()
    .zip(other.asSequence())
    .filter { it.first != '*' }
    .all { it.first == it.second }

inline val Byte.hex2 get() = "%02X".format(this)

inline val Short.hex2 get() = "%02X".format(this)
inline val Short.hex4 get() = "%04X".format(this)

inline val Int.hex2 get() = "%02X".format(this)
inline val Int.hex4 get() = "%04X".format(this)
inline val Int.hex8 get() = "%08X".format(this)

inline val Long.hex2 get() = "%02X".format(this)
inline val Long.hex4 get() = "%04X".format(this)
inline val Long.hex8 get() = "%08X".format(this)
inline val Long.hex16 get() = "%016X".format(this)

inline val Byte.lhex2 get() = "%02x".format(this)

inline val Short.lhex2 get() = "%02x".format(this)
inline val Short.lhex4 get() = "%04x".format(this)

inline val Int.lhex2 get() = "%02x".format(this)
inline val Int.lhex4 get() = "%04x".format(this)
inline val Int.lhex8 get() = "%08x".format(this)

inline val Long.lhex2 get() = "%02x".format(this)
inline val Long.lhex4 get() = "%04x".format(this)
inline val Long.lhex8 get() = "%08x".format(this)
inline val Long.lhex16 get() = "%016x".format(this)

inline val ULong.hex2 get() = "%02X".format(long)
inline val ULong.hex4 get() = "%04X".format(long)
inline val ULong.hex8 get() = "%08X".format(long)
inline val ULong.hex16 get() = "%016X".format(long)

inline val UInt.hex2 get() = "%02X".format(int)
inline val UInt.hex4 get() = "%04X".format(int)
inline val UInt.hex8 get() = "%08X".format(int)

inline val UShort.hex2 get() = "%02X".format(short)
inline val UShort.hex4 get() = "%04X".format(short)

inline val UByte.hex2 get() = "%02X".format(byte)

inline val Long.str get() = toString()
inline val Int.str get() = toString()
inline val Short.str get() = toString()
inline val Byte.str get() = toString()

inline val ULong.hex get() = when {
    this > 0xFFFF_FFFFu -> hex16
    this > 0xFFFFu -> hex8
    this > 0xFFu -> hex4
    else -> hex2
}

inline val UInt.hex get() = when {
    this > 0xFFFFu -> hex8
    this > 0xFFu -> hex4
    else -> hex2
}

inline val UShort.hex get() = when {
    this > 0xFFu -> hex4
    else -> hex2
}

inline val UByte.hex get() = hex2

inline val Long.hex get() = ulong.hex
inline val Int.hex get() = uint.hex
inline val Short.hex get() = ushort.hex
inline val Byte.hex get() = ubyte.hex

inline fun String.alignLeft(maxlen: Int = length) = "%${maxlen}s".format(this)

inline fun String.alignRight(maxlen: Int = length) = "%-${maxlen}s".format(this)

/**
 * Slice input string from 0 to maxlen.
 * If maxlen > string.length then remain string.length (no exception thrown)
 */
inline fun String.stretch(maxlen: Int, alignRight: Boolean = true) = when {
    length == 0 -> alignLeft(maxlen)
    alignRight -> slice(0 until length.coerceAtMost(maxlen)).alignRight(maxlen)
    else -> slice((length - maxlen).coerceAtLeast(0) until length).alignLeft(maxlen)
}

val emptyString: String = String()

/**
 * {EN}
 * Makes [File] from a [String]
 *
 * @return [File]
 * {EN}
 */
fun String.toFile() = File(this)

/**
 * {EN}
 * Makes [File] from [String] and append [child] to it.
 * Also checks if parent is blank avoid absolute path creation
 *
 * @param child Path suffix
 * @return [File]
 * {EN}
 */
fun String.toFile(child: String): File {
    // If parent is blank then absolute path will be created
    // "".toFile("temp") -> /temp
    if (isBlank()) return File(child)
    return File(this, child)
}

/**
 * {EN}
 * Checks whether current [String] is path to a some file on filesystem. Wrapper to [File.isFile].
 *
 * @return true if and only if the file denoted by this abstract pathname exists and is a normal file
 * {EN}
 */
fun String.isFile() = toFile().isFile

/**
 * {EN}
 * Checks whether current [String] is path to a some directory on filesystem. Wrapper to [File.isDirectory].
 *
 * @return true if and only if the file denoted by this abstract pathname exists and is a directory
 * {EN}
 */
fun String.isDirectory() = toFile().isDirectory

/**
 * {EN}
 * Checks whether current [String] is absolute path. Wrapper to [File.isAbsolute].
 *
 * @return true if this abstract pathname is absolute
 * {EN}
 */
fun String.isAbsolute() = toFile().isAbsolute

/**
 * {EN}
 * Creates the directory named by this abstract pathname, including any
 * necessary but nonexistent parent directories.  Note that if this
 * operation fails it may have succeeded in creating some of the necessary
 * parent directories.
 *
 * Wrapper to [File.mkdirs].
 *
 * @return true if and only if the directory was created, along with all necessary parent directories
 * {EN}
 */
fun String.mkdirs() = toFile().mkdirs()

/**
 * {EN}
 * Returns an array of abstract pathnames denoting the files in the
 * directory denoted by this abstract pathname.
 *
 * Wrapper to [File.listFiles].
 *
 * @return  An array of abstract pathnames denoting the files and
 *          directories in the directory denoted by this abstract pathname.
 *          The array will be empty if the directory is empty.  Throw [IllegalArgumentException]
 *          if this abstract pathname does not denote a directory, or if an I/O error occurs.
 * {EN}
 */
fun String.listdir(): Array<File> = toFile().listFiles() ?: throw IllegalArgumentException("Can't list directory $this!")

/**
 * {EN}
 * Returns an array of abstract pathnames denoting the files and
 * directories in the directory denoted by this abstract pathname that
 * satisfy the specified filter.
 *
 * Wrapper to [File.listFiles].
 *
 * @param predicate A file filter
 *
 * @return  An array of abstract pathnames denoting the files and
 *          directories in the directory denoted by this abstract pathname.
 *          The array will be empty if the directory is empty.  Throw [IllegalArgumentException]
 *          if this abstract pathname does not denote a directory, or if an I/O error occurs.
 * {EN}
 */
fun String.listdir(predicate: (File) -> Boolean): Array<File> = toFile().listFiles(predicate)
    ?: throw IllegalArgumentException("Can't list directory $this!")

/**
 * {EN}
 * Checks whether [String] has ends with specified [extension] and if no then add [extension] to [String]
 *
 * @param extension A suffix to append to [String]
 *
 * @return A string with suffix without duplicate it
 * {EN}
 */
fun String.addExtension(extension: String): String = if (!endsWith(extension)) this + extension else this


/**
 * {EN}
 * Operator function make possible to join paths using / operation between [File] and [String]
 * val base = File("base")
 * val fullpath = base / "test"
 *
 * @param child path suffix
 *
 * @return joined paths
 * {EN}
 */
operator fun String.div(child: String): String = toFile(child).path

/**
 * {EN}
 * Repeats [String] [n] times.
 *
 * @param n A number to repeat the string
 *
 * @return repeated [n] times string
 * {EN}
 */
operator fun String.times(n: Int) = repeat(n)

/**
 * Splits this char sequence around matches of the given regular expression and remove leading and trailing whitespace.
 *
 * @param regex A regex expression
 *
 * @return A list of trimmed strings split by [regex] expression
 */
@Deprecated("splitTrim is deprecated use splitBy(trim = true) instead")
fun String.splitTrim(regex: Regex) = split(regex).map { it.trim() }

/**
 * Splits this char sequence to a list of strings around occurrences of the
 * specified [delimiters] and remove leading and trailing whitespace.
 *
 * @param delimiters One or more strings to be used as delimiters.
 * @param ignoreCase `true` to ignore character case when matching a delimiter. By default `false`.
 *
 * @return A list of trimmed strings split by [delimiters]
 */
@Deprecated("splitTrim is deprecated use splitBy(trim = true) instead")
fun String.splitTrim(vararg delimiters: String, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase).map { it.trim() }

/**
 * Splits this char sequence to a list of strings by any whitespaces
 *
 * @return A list of strings split by any whitespace characters
 */
@Deprecated("splitWhitespaces is deprecated use splitBy(whitespaces) instead")
fun String.splitWhitespaces() = splitTrim(whitespaces)

/**
 * Returns regex to use in splitter for multiple spaces or tabs
 *
 * @since 0.3.4
 */
val whitespaces = Regex("\\s+")

/**
 * Splits this char sequence to a list of strings around occurrences of the
 * specified [regex] and:
 * - remove leading and trailing whitespace if required
 * - remove blank strings if required
 *
 * @param regex A regex expression
 * @param trim `true` to remove leading and trailing whitespaces
 * @param removeBlank `true` to remove blank strings after split
 *
 * @since 0.3.4
 */
fun String.splitBy(regex: Regex, trim: Boolean = true, removeBlank: Boolean = true): List<String> {
    var result = split(regex)
    if (trim) result = result.map { it.trim() }
    if (removeBlank) result = result.filter { it.isNotBlank() }
    return result
}

/**
 * Splits this char sequence to a list of strings around occurrences of the
 * specified [delimiters] and:
 * - remove leading and trailing whitespaces if required
 * - remove blank strings if required
 *
 * @param delimiters One or more strings to be used as delimiters
 * @param ignoreCase `true` to ignore character case when matching a delimiter. By default `false`
 * @param trim `true` to remove leading and trailing whitespaces
 * @param removeBlank `true` to remove blank strings after split
 *
 * @since 0.3.4
 */
fun String.splitBy(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    trim: Boolean = true,
    removeBlank: Boolean = true
): List<String> {
    var result = split(*delimiters, ignoreCase = ignoreCase)
    if (trim) result = result.map { it.trim() }
    if (removeBlank) result = result.filter { it.isNotBlank() }
    return result
}

/**
 * Returns a substring after the last occurrence of [start] and before the first occurrence of [end]
 * If the string does not contain the [start] or [end] throw an error.
 *
 * @param start delimiter after which start new string
 * @param end delimiter before which end new string
 *
 * @return A substring between [start] and [end]
 */
fun String.substringBetween(start: String, end: String): String {
    val startIndex = lastIndexOf(start)
    require(startIndex >= 0)
    val endIndex = indexOf(end)
    require(endIndex >= 0)
    return substring(startIndex + start.length, endIndex)
}

/**
 * Returns a substring with replaced from first [start] and after the last occurrence of [end] with [value]
 * If the string does not contain the [start] or [end] return original string.
 *
 * @param start delimiter before which start new string
 * @param end delimiter after which end new string
 *
 * @return A string with replaced text between from [start] to [end]
 */
fun String.replaceBetween(start: String, end: String, value: String): String {
    val startIndex = indexOf(start)
    if (startIndex < 0) return this
    val endIndex = lastIndexOf(end)
    if (endIndex < 0) return this
    return substring(0, startIndex) + value + substring(endIndex + end.length)
}

/**
 * Returns a substring before the first occurrence of [start] and after the last occurrence of [end]
 * If the string does not contain the [start] or [end] return original string.
 *
 * @param start delimiter before which start new string
 * @param end delimiter after which end new string
 *
 * @return A string without substring from [start] to [end]
 *
 * @since 0.3.4
 */
fun String.removeBetween(start: String, end: String) = replaceBetween(start, end, "")


/**
 * If this string starts with the given [prefix], returns a copy of this string
 * with the prefix removed. Otherwise, returns this null.
 *
 * @param prefix string to remove
 *
 * @since 0.3.6
 */
inline fun String.removePrefixOrNull(prefix: CharSequence): String? {
    val result = removePrefix(prefix)
    return if (this === result) null else result
}


inline fun String.classpathToPath() = replace(".", "/")

inline fun String.className() = substringAfterLast(".")

inline fun String.packageName() = substringBeforeLast(".")

inline fun String.splitPackageClass() = Pair(packageName(), className())

inline fun String.crop(maxlen: Int = 32) = if (length <= maxlen) this else "${take(maxlen)}..."

inline fun String.toFileOutputStream() = toFile().outputStream()

inline fun String.toFileInputStream() = toFile().inputStream()

inline fun String.toInetSocketAddress(): InetSocketAddress {
    val host = substringBefore(":")
    val port = substringAfter(":").int()
    return InetSocketAddress(host, port)
}