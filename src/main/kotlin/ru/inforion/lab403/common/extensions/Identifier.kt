@file:Suppress("NOTHING_TO_INLINE")

package ru.inforion.lab403.common.extensions

import org.bson.types.ObjectId
import ru.inforion.lab403.common.extensions.serialization.serialize
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.Serializable
import java.math.BigInteger

typealias Identifier = ObjectId

inline fun DataOutputStream.writeIdentifier(value: Identifier) = writeByteArray(value.toByteArray())

inline fun DataInputStream.readIdentifier() = Identifier(readByteArray())

inline fun ByteArray.toIdentifier() = Identifier(this)

inline fun String.toIdentifier() = Identifier(this)

inline fun ObjectId.toIdentifier() = this

inline fun Identifier.toBigInteger() = BigInteger(toByteArray())

inline fun identifierOf(vararg values: Serializable) = values
    .map { it.serialize(false) }
    .reduce { acc, bytes -> acc + bytes }
    .sha1()
    .copyOfRange(0, 12)
    .toIdentifier()

inline fun identifier() = Identifier()