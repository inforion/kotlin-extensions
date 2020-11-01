package ru.inforion.lab403.common.extensions.argparse.options

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.extensions.argparse.abstracts.AbstractOption

class File(
    help: String?,
    required: Boolean,
    val exists: Boolean,
    val canRead: Boolean,
    val canWrite: Boolean
) : AbstractOption<java.io.File>(help, required, null) {
    override fun inject(parser: ArgumentParser) = super.inject(parser).also {
        val type = Arguments.fileType()
        if (exists) type.verifyExists()
        if (canRead) type.verifyCanRead()
        if (canWrite) type.verifyCanWrite()
        it.type(type)
    }
}