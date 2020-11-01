package ru.inforion.lab403.common.extensions.argparse

import net.sourceforge.argparse4j.helper.HelpScreenException
import net.sourceforge.argparse4j.inf.Namespace
import ru.inforion.lab403.common.extensions.argparser
import ru.inforion.lab403.common.extensions.argparse.abstracts.AbstractOption
import kotlin.system.exitProcess

/**
 * Create new program options with specified [name], [description] and [defaultHelp] parameter
 *
 * @param name ArgumentParser name
 * @param description ArgumentParser description
 * @param defaultHelp ArgumentParser defaultHelp
 */
open class ApplicationOptions(val name: String, val description: String? = null, val defaultHelp: Boolean = true) {
    var initialized: Boolean = false
        private set

    lateinit var namespace: Namespace
        private set

    private val items = mutableListOf<AbstractOption<*>>()

    fun <T : AbstractOption<*>> add(construct: () -> T) = construct().also { items.add(it) }

    fun parse(args: Array<String>) {
        val parser = argparser(name, description, defaultHelp)

        val injected = items.map { it to it.inject(parser) }

        namespace = try {
            parser.parseArgs(args)
        } catch (exception: HelpScreenException) {
            exitProcess(0)
        }

        injected.forEach { (property, argument) -> property.extract(namespace, argument) }

        initialized = true
    }
}