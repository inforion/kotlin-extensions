@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.inforion.lab403.common.mongo

import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.UpdateOptions
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.Document
import ru.inforion.lab403.common.extensions.sure
import ru.inforion.lab403.common.identifier.Identifier
import ru.inforion.lab403.common.identifier.toIdentifier

inline fun BsonWriter.writeDocument(action: BsonWriter.() -> Unit) {
    writeStartDocument()
    action(this)
    writeEndDocument()
}

inline fun <R> BsonReader.readDocument(action: BsonReader.() -> R): R {
    readStartDocument()
    val result = action(this)
    readEndDocument()
    return result
}

inline fun Map<String, Any>.toDocument() = Document(this)

inline fun upsertReplace(): ReplaceOptions = ReplaceOptions().upsert(true)

inline fun upsertUpdate(): UpdateOptions = UpdateOptions().upsert(true)

inline fun id(id: Identifier) = Document("_id", id)

inline fun set(value: Document) = Document("\$set", value)

inline fun <R> Document.getList(key: String, transform: (Document) -> R) =
    getList(key, Document::class.java).map(transform)

inline fun <R> Document.getSet(key: String, transform: (Document) -> R) = getList(key, transform).toSet()

inline fun <R> Document.getMutableSet(key: String, transform: (Document) -> R) = getList(key, transform).toMutableSet()

inline fun Document.getIdentifierOrNull(key: String): Identifier? = getObjectId(key)?.toIdentifier()

inline fun Document.getIdentifier(key: String) = getIdentifierOrNull(key).sure { "Key $key was not found in document" }

inline fun <reified T : Enum<T>> Document.getEnumValue(key: String) = enumValueOf<T>(getString(key))