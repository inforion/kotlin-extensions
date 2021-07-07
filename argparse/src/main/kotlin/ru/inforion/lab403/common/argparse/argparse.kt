@file:Suppress("unused")

package ru.inforion.lab403.common.argparse

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.impl.action.StoreFalseArgumentAction
import net.sourceforge.argparse4j.impl.action.StoreTrueArgumentAction
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import java.util.*

/**
 * Java ArgumentParser library wrapper for simplify usage in Kotlin
 */

/**
 * Special signature for parser and subparser identification
 */
const val SUBPARSER_ID = "\$PARSER$"

/**
 * Checks whether or not specified [value] in parsed name space
 *
 * @param value name of value to check
 */
operator fun Namespace.contains(value: String?) = get<Any>(value) != null

/**
 * Create new argument parser with specified [name], [description] and [defaultHelp] parameter
 *
 * @param name ArgumentParser name
 * @param description ArgumentParser description
 * @param defaultHelp ArgumentParser defaultHelp
 */
fun argparser(name: String, description: String? = null, defaultHelp: Boolean = true): ArgumentParser {
    return ArgumentParsers.newArgumentParser(name).apply {
        defaultHelp(defaultHelp)
        if (description != null)
            description(description)
    }
}

internal fun ArgumentParser.getIdentifier() = getDefault(SUBPARSER_ID)
internal fun ArgumentParser.setIdentifier(value: String) = setDefault(SUBPARSER_ID, value)

/**
 * Add subparser to parser
 *
 * @param name subparser name
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
fun ArgumentParser.subparser(name: String, help: String? = null): ArgumentParser {
    val subparsers = addSubparsers()
    val parser: Subparser = subparsers.addParser(name)
    if (help != null)
        parser.help(help)
    val parentId = getIdentifier()
    if (parentId != null)
        parser.setIdentifier("$parentId.$name")
    else
        parser.setIdentifier("root.$name")
    return parser
}

/**
 * Add flag argument to argument parser
 *
 * @param short short flag name
 * @param long long flag name (will be used for namespace after parse)
 * @param help help string
 * @param default default value for flag
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
fun ArgumentParser.flag(short: String, long: String, help: String? = null, default: Boolean = false) {
    val action = if (!default) StoreTrueArgumentAction() else StoreFalseArgumentAction()
    val arg = addArgument(short, long).action(action)
    if (help != null)
        arg.help(help)
}

/**
 * Add choices argument to argument parser using predefined Enum<T> type
 *
 * @param name choices parameter name (will be used for namespace after parse)
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
inline fun <reified T: Enum<T>>ArgumentParser.choices(name: String, help: String? = null) =
        choices(name, enumValues<T>().map { it.toString() }, help)

/**
 * Add choices argument to argument parser using predefined Enum<T> type
 *
 * @param short short choices name
 * @param long long choices name (will be used for namespace after parse)
 * @param required is parameter required
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
inline fun <reified T: Enum<T>>ArgumentParser.choices(short: String, long: String, required: Boolean = false, help: String? = null) =
        choices(short, long, enumValues<T>().map { it.toString() }, required, help)

/**
 * Add choices argument to argument parser using strings collection
 *
 * @param name choices parameter name (will be used for namespace after parse)
 * @param choices values of choice
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
fun ArgumentParser.choices(name: String, choices: Collection<String>, help: String? = null) {
    val arg = addArgument(name).choices(choices)
    if (help != null)
        arg.help(help)
}

/**
 * Add choices argument to argument parser using strings collection
 *
 * @param short short choices name
 * @param long long choices name (will be used for namespace after parse)
 * @param required is parameter required
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
fun ArgumentParser.choices(short: String, long: String, choices: Collection<String>, required: Boolean = false, help: String? = null) {
    val arg = addArgument(short, long).choices(choices)
    if (help != null)
        arg.help(help)
    arg.required(required)
}

/**
 * Add a position argument
 *
 * @param name argument name (will be used for namespace after parse)
 * @param default default argument value
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
inline fun <reified T>ArgumentParser.variable(name: String, default: T? = null, help: String? = null) {
    val arg = addArgument(name).type(T::class.java)

    if (default != null)
        arg.default = default

    if (help != null)
        arg.help(help)
}

/**
 * Add named argument
 *
 * @param short short variable name
 * @param long long variable name (will be used for namespace after parse)
 * @param default default argument value
 * @param required is parameter required
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
inline fun <reified T>ArgumentParser.variable(
        short: String,
        long: String,
        default: T? = null,
        required: Boolean = false,
        help: String? = null) {
    val arg = addArgument(short, long).type(T::class.java)

    arg.required(required)

    if (default != null)
        arg.default = default

    if (help != null)
        arg.help(help)
}

/**
 * Add vararg argument
 *
 * @param name argument name (will be used for namespace after parse)
 * @param count variable argument count (if -1 - unspecified)
 * @param help help string
 */
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
inline fun <reified T>ArgumentParser.varags(name: String, count: Int = -1, help: String? = null) {
    val arg = addArgument(name)
            .type(T::class.java)

    if (count == -1)
        arg.nargs("+")
    else
        arg.nargs(count)

    if (help != null)
        arg.help(help)
}

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
@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
fun ArgumentParser.file(
        short: String,
        long: String,
        exists: Boolean = false,
        canRead: Boolean = false,
        canWrite: Boolean = false,
        required: Boolean = false,
        help: String? = null
) {
    val typ = Arguments.fileType()
    if (exists) typ.verifyExists()
    if (canRead) typ.verifyCanRead()
    if (canWrite) typ.verifyCanWrite()

    val arg = addArgument(short, long).type(typ)

    arg.required(required)

    if (help != null)
        arg.help(help)
}

/**
 * Parse [args] arguments with handling error and print information about error
 *
 * @param args arguments string list
 */
private fun ArgumentParser.parseIntern(args: List<String>) = try {
    parseArgs(args.toTypedArray())
} catch (error: ArgumentParserException) {
    handleError(error)
    null
}

/**
 * Parse [args] arguments in range [range]
 *
 * @param args arguments string list
 * @param range range of arguments in list to be parsed
 */
fun ArgumentParser.parse(args: List<String>, range: IntRange) = parseIntern(args.slice(range))

/**
 * Parse [args] arguments started from [from]
 *
 * @param args arguments string list
 * @param from start index in list to parse
 */
fun ArgumentParser.parse(args: List<String>, from: Int) = parseIntern(args.drop(from))

/**
 * Parse [args] arguments
 *
 * @param args arguments string list
 */
fun ArgumentParser.parse(args: List<String>) = parseIntern(args)

/**
 * Parse [args] arguments
 *
 * @param args arguments string
 */
fun ArgumentParser.parse(args: String) = parseIntern(args.split(' ').map { it.trim() })

@Deprecated("ArgumentParser extensions deprecated since 0.3.4", replaceWith = ReplaceWith("ApplicationOptions"))
fun Namespace.getParserCommandStack() = LinkedList(getString(SUBPARSER_ID).split("."))