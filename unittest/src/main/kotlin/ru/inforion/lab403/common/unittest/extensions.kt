@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.unittest

import unsigned.types.*
import kotlin.test.assertEquals

inline fun assertByte(expected: Byte, actual: Byte) = assertEquals(expected, actual)
inline fun assertShort(expected: Short, actual: Short) = assertEquals(expected, actual)
inline fun assertInt(expected: Int, actual: Int) = assertEquals(expected, actual)
inline fun assertLong(expected: Long, actual: Long) = assertEquals(expected, actual)

inline fun assertUByte(expected: UByte, actual: UByte) = assertEquals(expected, actual)
inline fun assertUShort(expected: UShort, actual: UShort) = assertEquals(expected, actual)
inline fun assertUInt(expected: UInt, actual: UInt) = assertEquals(expected, actual)
inline fun assertULong(expected: ULong, actual: ULong) = assertEquals(expected, actual)
