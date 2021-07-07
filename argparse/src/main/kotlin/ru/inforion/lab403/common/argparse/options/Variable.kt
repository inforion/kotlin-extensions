package ru.inforion.lab403.common.argparse.options

import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.argparse.abstracts.AbstractOption
import ru.inforion.lab403.common.argparse.ValueGetter
import kotlin.reflect.KClass

class Variable<T : Any> constructor(
    help: String?,
    required: Boolean,
    default: ValueGetter<T>?,
    val type: KClass<out T>
) : AbstractOption<T>(help, required, default) {
    override fun inject(parser: ArgumentParser) = super.inject(parser).type(type.java)
}