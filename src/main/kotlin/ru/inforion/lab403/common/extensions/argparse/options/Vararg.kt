package ru.inforion.lab403.common.extensions.argparse.options

import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.extensions.argparse.abstracts.AbstractOption
import kotlin.reflect.KClass

class Vararg<T : Any> constructor(
    help: String?,
    val count: Int,
    val type: KClass<out T>
) : AbstractOption<T>(help, false, null) {
    override fun inject(parser: ArgumentParser) =
        super.inject(parser).type(type.java).also {
            if (count == -1) it.nargs("+") else it.nargs(count)
        }
}