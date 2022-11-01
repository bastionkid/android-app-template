package com.azuredragon.core.localstorage.settings

import kotlinx.coroutines.flow.Flow

interface Settings {

    fun getInt(key: String, default: Int = Int.MIN_VALUE): Flow<Int>

    fun getLong(key: String, default: Long = Long.MIN_VALUE): Flow<Long>

    fun getFloat(key: String, default: Float = Float.MIN_VALUE): Flow<Float>

    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean>

    fun getString(key: String, default: String? = null): Flow<String?>

    fun getStringSet(key: String, default: Set<String>? = null): Flow<Set<String>?>

    suspend fun putInt(key: String, value: Int)

    suspend fun putLong(key: String, value: Long)

    suspend fun putFloat(key: String, value: Float)

    suspend fun putBoolean(key: String, value: Boolean)

    suspend fun putString(key: String, value: String?)

    suspend fun putStringSet(key: String, value: Set<String>?)

    suspend fun removeInt(key: String)

    suspend fun removeLong(key: String)

    suspend fun removeFloat(key: String)

    suspend fun removeBoolean(key: String)

    suspend fun removeString(key: String)

    suspend fun removeStringSet(key: String)

    suspend fun clearAll()
}