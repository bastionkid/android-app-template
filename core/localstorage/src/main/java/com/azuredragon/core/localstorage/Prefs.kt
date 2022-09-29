package com.azuredragon.core.localstorage

import kotlinx.coroutines.flow.Flow

interface Prefs {

    fun getInt(key: String, default: Int = Int.MIN_VALUE): Flow<Int>

    fun getLong(key: String, default: Long = Long.MIN_VALUE): Flow<Long>

    fun getFloat(key: String, default: Float = Float.MIN_VALUE): Flow<Float>

    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean>

    fun getString(key: String, default: String? = null): Flow<String?>

    fun getStringSet(key: String, default: Set<String>? = null): Flow<Set<String>?>
}