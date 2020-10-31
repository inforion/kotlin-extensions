package ru.inforion.lab403.common.extensions.argparse.options

import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.extensions.argparse.abstracts.AbstractOption
import ru.inforion.lab403.common.extensions.argparse.ValueGetter
import kotlin.reflect.KClass

class Choices<E : Enum<E>> constructor(
    help: String?,
    required: Boolean,
    default: ValueGetter<E>?,
    val choices: Array<E>,
    val type: KClass<out E>
) : AbstractOption<E>(help, required, default) {
    override fun inject(parser: ArgumentParser) = super.inject(parser).type(type.java).choices(*choices)
}