package ru.inforion.lab403.common.extensions

operator fun Int.times(string: String) = string.repeat(this)

// https://stackoverflow.com/questions/7446710/how-to-round-up-integer-division-and-have-int-result-in-java
infix fun Int.ceil(divisor: Int) = this / divisor + if (this % divisor == 0) 0 else 1

infix fun Long.ceil(divisor: Long) = this / divisor + if (this % divisor == 0L) 0 else 1

infix fun Int.floor(divisor: Int) = this / divisor

infix fun Long.floor(divisor: Int) = this / divisor