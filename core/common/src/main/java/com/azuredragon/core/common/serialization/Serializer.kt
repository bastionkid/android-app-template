package com.azuredragon.core.common.serialization

import kotlin.reflect.KClass

interface Serializer {

	fun <T : Any> fromJson(jsonString: String, clazz: KClass<T>): T

	fun <T : Any> listFromJson(jsonString: String, clazz: KClass<T>): List<T>

	fun <T : Any> toJson(data: T, clazz: KClass<T>): String
}
