@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import kotlin.experimental.and

/**
 * Created by Alexei Gladkikh on 20/06/16.
 *
 * Extension methods and function for working with Strings and transformation
 */
operator fun String.get(range: IntRange): String {
    val _r = if (range.last < 0) range.start..this.length + range.last else range
    return this.slice(_r.start until _r.last)
}

fun String.toCamelCase(): String {
    if (this.isNotEmpty())
        return this[0].toLowerCase() + this.substring(1)
    return this
}

fun ByteArray.ascii(): String = map { if (it in 0x20..0x7e) it.toChar() else '.' }.joinToString("")

fun ByteArray.hexlify(upperCase: Boolean = true, separator: Char? = null): String {
    val hexChars = CharArray(this.size * 2)
    for (j in 0 until this.size) {
        val v = this[j].toInt()
        hexChars[j * 2] = Character.forDigit(v[7..4], 16)
        hexChars[j * 2 + 1] = Character.forDigit(v[3..0], 16)
    }
    val result = if (upperCase) String(hexChars).toUpperCase() else String(hexChars)
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
        data[k / 2] = (ch0.shl(4) or ch1).toByte()
    }
    return data
}

fun Long.sbits(bitSize: Int): String = (bitSize - 1 downTo 0).joinToString("") { "${this[it]}" }

val Long.sbits get() = sbits(64)
val Int.sbits get() = asLong.sbits(32)
val Short.sbits get() = asLong.sbits(16)
val Byte.sbits get() = asLong.sbits(8)

fun ByteArray.decode(charset: Charset = Charsets.UTF_8): String {
    var size = 0
    while (this[size].toInt() != 0) size++
    return String(this, 0, size, charset)
}

fun String.toUnhexlifyByteBuffer(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteBuffer {
    return ByteBuffer.wrap(this.unhexlify()).apply { order(byteOrder) }
}

fun String.toLong(radix: Int = 10): Long = java.lang.Long.parseLong(this, radix)
fun String.toULong(radix: Int = 10): Long = java.lang.Long.parseUnsignedLong(this, radix)
fun String.toInt(radix: Int = 10): Int = Integer.parseInt(this, radix)
fun String.toUInt(radix: Int = 10): Int = Integer.parseUnsignedInt(this, radix)
fun String.toShort(radix: Int = 10): Short = java.lang.Short.parseShort(this, radix)
fun String.toByte(radix: Int = 10): Byte = java.lang.Byte.parseByte(this, radix)


val String.hexAsInt get() = java.lang.Integer.parseInt(this, 16)
val String.hexAsUInt get() = java.lang.Integer.parseUnsignedInt(this, 16)
val String.hexAsULong get() = java.lang.Long.parseUnsignedLong(this, 16)


@Deprecated("Use property syntax", replaceWith = ReplaceWith("hexAsInt"))
fun String.toIntFromHex(): Int = Integer.parseInt(this, 16)

@Deprecated("Use property syntax", replaceWith = ReplaceWith("hexAsUInt"))
fun String.toUIntFromHex(): Int = Integer.parseUnsignedInt(this, 16)

@Deprecated("Use property syntax", replaceWith = ReplaceWith("hexAsULong"))
fun String.toULongFromHex(): Long = java.lang.Long.parseUnsignedLong(this, 16)

@Deprecated("Use hex1, hex2, hex4 or hex8 properties instead", replaceWith = ReplaceWith("hex4"))
fun Number.toHexString(): String = "%08X".format(this)


infix fun String.resembles(other: String): Boolean {
    return this.asSequence().zip(other.asSequence()).filter { it.first != '*' }.all { it.first == it.second }
}

/**
 * Returns string of hex representation of byte value with only 2 tetrades
 */
inline val Byte.hex2 get() = "%02X".format(this)

/**
 * Returns string of hex representation of short value with only 2 tetrades
 */
inline val Short.hex2 get() = "%02X".format(this and 0xFF)
/**
 * Returns string of hex representation of short value with only 4 tetrades
 */
inline val Short.hex4 get() = "%04X".format(this and -1)

/**
 * Returns string of hex representation of int value with only 2 tetrades
 */
inline val Int.hex2 get() = "%02X".format(this and 0xFF)
/**
 * Returns string of hex representation of int value with only 4 tetrades
 */
inline val Int.hex4 get() = "%04X".format(this and 0xFFFF)
/**
 * Returns string of hex representation of int value with only 8 tetrades
 */
inline val Int.hex8 get() = "%08X".format(this and -1)

/**
 * Returns string of hex representation of long value with only 2 tetrades
 */
inline val Long.hex2 get() = "%02X".format(this and 0xFF)
/**
 * Returns string of hex representation of long value with only 4 tetrades
 */
inline val Long.hex4 get() = "%04X".format(this and 0xFFFF)
/**
 * Returns string of hex representation of long value with only 8 tetrades
 */
inline val Long.hex8 get() = "%08X".format(this and 0xFFFF_FFFF)
/**
 * Returns string of hex representation of long value with only 16 tetrades
 */
inline val Long.hex16 get() = "%016X".format(this)

/**
 * Returns lower-case string of hex representation of byte value with only 2 tetrades
 */
inline val Byte.lhex2 get() = "%02x".format(this)

/**
 * Returns lower-case string of hex representation of short value with only 2 tetrades
 */
inline val Short.lhex2 get() = "%02x".format(this and 0xFF)
/**
 * Returns lower-case string of hex representation of short value with only 4 tetrades
 */
inline val Short.lhex4 get() = "%04x".format(this and -1)

/**
 * Returns lower-case string of hex representation of int value with only 2 tetrades
 */
inline val Int.lhex2 get() = "%02x".format(this and 0xFF)
/**
 * Returns lower-case string of hex representation of int value with only 4 tetrades
 */
inline val Int.lhex4 get() = "%04x".format(this and 0xFFFF)
/**
 * Returns lower-case string of hex representation of int value with only 8 tetrades
 */
inline val Int.lhex8 get() = "%08x".format(this and -1)

/**
 * Returns lower-case string of hex representation of long value with only 2 tetrades
 */
inline val Long.lhex2 get() = "%02x".format(this and 0xFF)
/**
 * Returns lower-case string of hex representation of long value with only 4 tetrades
 */
inline val Long.lhex4 get() = "%04x".format(this and 0xFFFF)
/**
 * Returns lower-case string of hex representation of long value with only 8 tetrades
 */
inline val Long.lhex8 get() = "%08x".format(this and 0xFFFF_FFFF)
/**
 * Returns lower-case string of hex representation of long value with only 16 tetrades
 */
inline val Long.lhex16 get() = "%016x".format(this)

inline val Long.str get() = toString()
inline val Int.str get() = toString()
inline val Short.str get() = toString()
inline val Byte.str get() = toString()


inline val Long.hex get() = when {
    this > 0xFFFF_FFFF -> hex16
    this > 0xFFFF -> hex8
    this > 0xFF -> hex4
    else -> hex2
}

inline val Int.hex get() = when {
    this > 0xFFFF -> hex8
    this > 0xFF -> hex4
    else -> hex2
}

inline val Short.hex get() = when {
    this > 0xFF -> hex4
    else -> hex2
}

inline val Byte.hex get() = hex2

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
    return substring(startIndex + 1, endIndex)
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
 */
fun String.removeBetween(start: String, end: String) = replaceBetween(start, end, "")


inline fun String.classpathToPath() = replace(".", "/")

inline fun String.className() = substringAfterLast(".")

inline fun String.packageName() = substringBeforeLast(".")

inline fun String.splitPackageClass() = Pair(packageName(), className())