@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.optional

inline val <T: Any> T?.optional get() = if (this != null) Optional.of(this) else Optional.empty()