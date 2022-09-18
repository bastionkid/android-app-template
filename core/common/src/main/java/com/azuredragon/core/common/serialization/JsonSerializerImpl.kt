package com.azuredragon.core.common.serialization

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class JsonSerializerImpl(
    private val json: Json,
): Serializer {

    @OptIn(InternalSerializationApi::class)
    override fun <T: Any> fromJson(jsonString: String, clazz: KClass<T>): T {
        return json.decodeFromString(clazz.serializer(), jsonString)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <T: Any> listFromJson(jsonString: String, clazz: KClass<T>): List<T> {
        return json.decodeFromString(ListSerializer(clazz.serializer()), jsonString)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <T: Any> toJson(data: T, clazz: KClass<T>): String {
        return json.encodeToString(clazz.serializer(), data)
    }
}