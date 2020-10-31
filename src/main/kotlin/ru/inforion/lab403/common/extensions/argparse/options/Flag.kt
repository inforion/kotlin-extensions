package ru.inforion.lab403.common.extensions.argparse.options

import net.sourceforge.argparse4j.impl.action.StoreFalseArgumentAction
import net.sourceforge.argparse4j.impl.action.StoreTrueArgumentAction
import net.sourceforge.argparse4j.inf.ArgumentParser
import ru.inforion.lab403.common.extensions.argparse.abstracts.AbstractOption

class Flag(help: String?, default: Boolean) : AbstractOption<Boolean>(help, false, { default }) {
    private val action get() = if (default!!.invoke()) StoreFalseArgumentAction() else StoreTrueArgumentAction()

    override fun inject(parser: ArgumentParser) = super.inject(parser).action(action)
}