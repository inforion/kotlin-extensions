package ru.inforion.lab403.common.extensions

const val WRONGL = -1L
const val WRONGI = -1
const val UNDEF = -1

const val INT64MASK = -1L
const val INT32MASK = 0xFFFF_FFFF
const val INT16MASK = 0xFFFF
const val INT8MASK = 0xFF
const val INT1MASK = 1

const val maxLongValue = Long.MAX_VALUE
const val minLongValue = Long.MIN_VALUE

const val maxIntValue = Int.MAX_VALUE
const val minIntValue = Int.MIN_VALUE

const val maxUIntValue = INT32MASK
const val minUIntValue = 0L

const val maxShortValue = Short.MAX_VALUE
const val minShortValue = Short.MIN_VALUE

const val maxUShortValue = INT16MASK
const val minUShortValue = 0

const val maxByteValue = Byte.MAX_VALUE
const val minByteValue = Byte.MIN_VALUE

const val maxUByteValue = INT8MASK
const val minUByteValue = 0

val longRange = minLongValue..maxLongValue
val intRange = minIntValue..maxIntValue
val shortRange = minShortValue..maxShortValue
val byteRange = minByteValue..maxByteValue