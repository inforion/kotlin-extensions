package ru.inforion.lab403.common.argparse.options

import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.argparse.ValueGetter
import ru.inforion.lab403.common.argparse.abstracts.AbstractOption
import kotlin.reflect.KClass

class Vararg<T : Any, C: List<T>> constructor(
    help: String?,
    required: Boolean,
    default: ValueGetter<C>?,
    val count: Int,
    val type: KClass<out T>
) : AbstractOption<C>(help, required, default) {
    override fun inject(parser: ArgumentParser) =
        super.inject(parser).type(type.java).also {
            if (count == -1) it.nargs("+") else it.nargs(count)
        }
}