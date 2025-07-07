package app.dvpn.libtailscale.internal.api.serialization

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Any")

    override fun serialize(encoder: Encoder, value: Any) {
        val jsonEncoder = encoder as JsonEncoder
        val jsonElement = when (value) {
            is String -> JsonPrimitive(value)
            is Int -> JsonPrimitive(value)
            is Long -> JsonPrimitive(value)
            is Double -> JsonPrimitive(value)
            is Float -> JsonPrimitive(value)
            is Boolean -> JsonPrimitive(value)
            is ByteArray -> JsonPrimitive(Base64.encode(value))
            is IntArray -> JsonArray(value.map { JsonPrimitive(it) })
            is LongArray -> JsonArray(value.map { JsonPrimitive(it) })
            is FloatArray -> JsonArray(value.map { JsonPrimitive(it) })
            is DoubleArray -> JsonArray(value.map { JsonPrimitive(it) })
            is BooleanArray -> JsonArray(value.map { JsonPrimitive(it) })
            is List<*> -> JsonArray(value.map { serialize(it ?: JsonNull) })
            is Map<*, *> -> JsonObject(value.entries.associate {
                it.key.toString() to serialize(it.value ?: JsonNull)
            })
            else -> JsonPrimitive(value.toString())
        }
        jsonEncoder.encodeJsonElement(jsonElement)
    }

    private fun serialize(value: Any?): JsonElement = when (value) {
        null -> JsonNull
        is String -> JsonPrimitive(value)
        is Number -> JsonPrimitive(value)
        is Boolean -> JsonPrimitive(value)
        is ByteArray -> JsonPrimitive(Base64.encode(value))
        is List<*> -> JsonArray(value.map { serialize(it) })
        is Map<*, *> -> JsonObject(value.entries.associate {
            it.key.toString() to serialize(it.value)
        })
        else -> JsonPrimitive(value.toString())
    }

    override fun deserialize(decoder: Decoder): Any {
        val jsonDecoder = decoder as JsonDecoder
        val element = jsonDecoder.decodeJsonElement()
        return deserialize(element)
    }

    private fun deserialize(element: JsonElement): Any = when (element) {
        is JsonPrimitive -> when {
            element.isString -> element.content
            element.booleanOrNull != null -> element.boolean
            element.longOrNull != null -> element.long
            element.doubleOrNull != null -> element.double
            else -> element.content
        }
        is JsonArray -> element.map { deserialize(it) }
        is JsonObject -> element.mapValues { deserialize(it.value) }
        JsonNull -> throw SerializationException("Cannot deserialize null as Any")
    }
}
