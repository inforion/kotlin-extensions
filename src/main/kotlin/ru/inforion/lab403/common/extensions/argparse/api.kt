package ru.inforion.lab403.common.extensions.argparse

import ru.inforion.lab403.common.extensions.argparse.options.*
import ru.inforion.lab403.common.extensions.splitBy
import ru.inforion.lab403.common.extensions.whitespaces

typealias ValueGetter<T> = () -> T

/**
 * Adds a named argument
 *
 * @param short short variable name
 * @param long long variable name (will be used for namespace after parse)
 * @param default default argument value
 * @param required is parameter required
 * @param help help string
 *
 * @since 0.3.4
 */
inline fun <reified T : Any> ApplicationOptions.variable(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    noinline default: ValueGetter<T>? = null
) = add { Variable(help, required, default, T::class).also { it.nameOrFlags(short, long) } }

/**
 * Adds a position argument
 *
 * @param name argument name or long option if started from "-"/"--" (will be used for namespace after parse)
 * @param default default argument value
 * @param help help string
 *
 * @since 0.3.4
 */
inline fun <reified T : Any> ApplicationOptions.variable(
    name: String,
    help: String? = null,
    required: Boolean = false,
    noinline default: ValueGetter<T>? = null
) = add { Variable(help, required, default, T::class).also { it.nameOrFlags(name) } }

/**
 * Adds choices argument to argument parser using predefined Enum<T> type
 *
 * @param short short choice name
 * @param long long choice name (will be used for namespace after parse)
 * @param help help string
 *
 * @since 0.3.4
 */
inline fun <reified E : Enum<E>> ApplicationOptions.choices(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    noinline default: ValueGetter<E>? = null
) = add { Choices(help, required, default, enumValues(), E::class).also { it.nameOrFlags(short, long) } }

/**
 * Adds choices argument to argument parser using predefined Enum<T> type
 *
 * @param name argument name or long option if started from "-"/"--" (will be used for namespace after parse)
 * @param help help string
 * @param default default value for choice
 *
 * @since 0.3.4
 */
inline fun <reified E : Enum<E>> ApplicationOptions.choices(
    name: String,
    help: String? = null,
    required: Boolean = false,
    noinline default: ValueGetter<E>? = null
) = add { Choices(help, required, default, enumValues(), E::class).also { it.nameOrFlags(name) } }

/**
 * Adds flag argument to argument parser
 *
 * @param short short flag name
 * @param long long flag name (will be used for namespace after parse)
 * @param help help string
 * @param default default value for flag
 *
 * @since 0.3.4
 */
fun ApplicationOptions.flag(
    short: String,
    long: String,
    help: String? = null,
    default: Boolean = false
) = add { Flag(help, default).also { it.nameOrFlags(short, long) } }

/**
 * Add file argument
 *
 * @param short short file argument name
 * @param long long file argument name (will be used for namespace after parse)
 * @param exists check whether file exists
 * @param canRead check whether file can be read
 * @param canWrite check whether file can be written
 * @param required is parameter required
 * @param help help string
 *
 * @since 0.3.4
 */
fun ApplicationOptions.file(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    exists: Boolean = false,
    canRead: Boolean = false,
    canWrite: Boolean = false
) = add { File(help, required, exists, canRead, canWrite).also { it.nameOrFlags(short, long) } }

/**
 * Adds vararg (space separated list) argument
 *
 * @param name argument name or long option if started from "-"/"--" (will be used for namespace after parse)
 * @param help help string
 * @param required is parameter required
 * @param default default value for flag
 * @param count variable argument count (if -1 - unspecified)
 *
 * @since 0.3.4
 */
inline fun <reified T : Any, C: List<T>> ApplicationOptions.nargs(
    name: String,
    help: String? = null,
    required: Boolean = false,
    count: Int = -1,
    noinline default: ValueGetter<C>? = null
) = add { Vararg(help, required, default, count, T::class).also { it.nameOrFlags(name) } }

/**
 * Adds vararg (space separated list) argument
 *
 * @param short short vararg argument name
 * @param long long vararg argument name (will be used for namespace after parse)
 * @param help help string
 * @param required is parameter required
 * @param default default value for flag
 * @param count variable argument count (if -1 - unspecified)
 *
 * @since 0.3.5
 */
inline fun <reified T : Any, C: List<T>> ApplicationOptions.nargs(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    count: Int = -1,
    noinline default: ValueGetter<C>? = null
) = add { Vararg(help, required, default, count, T::class).also { it.nameOrFlags(short, long) } }

/**
 * Parse [this] array as specified arguments class [T]
 *
 * @since 0.3.4
 */
inline fun <reified T: ApplicationOptions> Array<String>.parseArguments() =
    T::class.java.getDeclaredConstructor().newInstance().also { it.internals.parse(this) }

/**
 * Parse [this] list as specified arguments class [T]
 *
 * @since 0.3.4
 */
inline fun <reified T: ApplicationOptions> List<String>.parseArguments() = toTypedArray().parseArguments<T>()

/**
 * Parse [this] string as specified arguments class [T]
 *
 * @since 0.3.4
 */
inline fun <reified T: ApplicationOptions> String.parseArguments() =
    splitBy(whitespaces).toTypedArray().parseArguments<T>()