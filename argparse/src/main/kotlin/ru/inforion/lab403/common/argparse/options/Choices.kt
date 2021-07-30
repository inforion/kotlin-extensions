package ru.inforion.lab403.common.argparse.options

import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.argparse.ValueGetter
import ru.inforion.lab403.common.argparse.abstracts.AbstractOption
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