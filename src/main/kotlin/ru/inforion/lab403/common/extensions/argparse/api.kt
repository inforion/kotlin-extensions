package ru.inforion.lab403.common.extensions.argparse

import ru.inforion.lab403.common.extensions.argparse.options.*

typealias ValueGetter<T> = () -> T

/**
 * Adds a named argument
 *
 * @param short short variable name
 * @param long long variable name (will be used for namespace after parse)
 * @param default default argument value
 * @param required is parameter required
 * @param help help string
 */
inline fun <reified T : Any> ApplicationOptions.variable(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    noinline default: ValueGetter<T>? = null
) = add { Variable(help, required, default, T::class).also { it.optional(short, long) } }

/**
 * Adds a position argument
 *
 * @param name argument name (will be used for namespace after parse)
 * @param default default argument value
 * @param help help string
 */
inline fun <reified T : Any> ApplicationOptions.variable(
    name: String,
    help: String? = null,
    noinline default: ValueGetter<T>? = null
) = add { Variable(help, true, default, T::class).also { it.positional(name) } }

/**
 * Adds choices argument to argument parser using predefined Enum<T> type
 *
 * @param short short choice name
 * @param long long choice name (will be used for namespace after parse)
 * @param help help string
 */
inline fun <reified E : Enum<E>> ApplicationOptions.choices(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    noinline default: ValueGetter<E>? = null
) = add { Choices(help, required, default, enumValues(), E::class).also { it.optional(short, long) } }

/**
 * Adds choices argument to argument parser using predefined Enum<T> type
 *
 * @param name choices parameter name (will be used for namespace after parse)
 * @param help help string
 * @param default default value for choice
 */
inline fun <reified E : Enum<E>> ApplicationOptions.choices(
    name: String,
    help: String? = null,
    noinline default: ValueGetter<E>? = null
) = add { Choices(help, true, default, enumValues(), E::class).also { it.positional(name) } }

/**
 * Adds flag argument to argument parser
 *
 * @param short short flag name
 * @param long long flag name (will be used for namespace after parse)
 * @param help help string
 * @param default default value for flag
 */
fun ApplicationOptions.flag(short: String, long: String, help: String? = null, default: Boolean = false) =
    add { Flag(help, default).also { it.optional(short, long) } }

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
 */
fun ApplicationOptions.file(
    short: String,
    long: String,
    help: String? = null,
    required: Boolean = false,
    exists: Boolean = false,
    canRead: Boolean = false,
    canWrite: Boolean = false
) = add { File(help, required, exists, canRead, canWrite).also { it.optional(short, long) } }

/**
 * Adds vararg argument
 *
 * @param name argument name (will be used for namespace after parse)
 * @param count variable argument count (if -1 - unspecified)
 * @param help help string
 */
inline fun <reified T : Any> ApplicationOptions.nargs(name: String, help: String? = null, count: Int = -1) =
    add { Vararg(help, count, T::class).also { it.positional(name) } }

inline fun <reified T: ApplicationOptions> Array<String>.parseArguments() =
    T::class.java.getDeclaredConstructor().newInstance().also { it.parse(this) }