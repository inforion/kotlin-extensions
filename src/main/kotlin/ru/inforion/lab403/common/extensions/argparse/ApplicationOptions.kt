package ru.inforion.lab403.common.extensions.argparse

import net.sourceforge.argparse4j.helper.HelpScreenException
import net.sourceforge.argparse4j.inf.Namespace
import ru.inforion.lab403.common.extensions.argparser
import ru.inforion.lab403.common.extensions.argparse.abstracts.AbstractOption
import kotlin.system.exitProcess

/**
 * Create new program options with specified [application], [description] and [help] parameter
 *
 * @param application ArgumentParser name
 * @param description ArgumentParser description
 * @param help ArgumentParser defaultHelp
 *
 * @since 0.3.4
 */
open class ApplicationOptions(application: String, description: String? = null, help: Boolean = true) {

    inner class Internals(
        val application: String,
        val description: String?,
        val help: Boolean
    ) {
        var initialized: Boolean = false
            private set

        lateinit var args: Array<String>
            private set

        lateinit var namespace: Namespace
            private set

        fun parse(args: Array<String>) {
            this.args = args

            val parser = argparser(application, description, help)

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

    val internals = Internals(application, description, help)

    private val items = mutableListOf<AbstractOption<*>>()

    fun <T : AbstractOption<*>> add(construct: () -> T) = construct().also { items.add(it) }

    override fun toString() = internals.namespace.toString()
}