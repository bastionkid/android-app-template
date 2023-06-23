package com.azuredragon.core.localstorage.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsImpl(private val context: Context) : Settings {

	override fun getInt(key: String, default: Int): Flow<Int> {
		return context.dataStore.data.map { it[intPreferencesKey(key)] ?: default }
	}

	override fun getLong(key: String, default: Long): Flow<Long> {
		return context.dataStore.data.map { it[longPreferencesKey(key)] ?: default }
	}

	override fun getFloat(key: String, default: Float): Flow<Float> {
		return context.dataStore.data.map { it[floatPreferencesKey(key)] ?: default }
	}

	override fun getBoolean(key: String, default: Boolean): Flow<Boolean> {
		return context.dataStore.data.map { it[booleanPreferencesKey(key)] ?: default }
	}

	override fun getString(key: String, default: String?): Flow<String?> {
		return context.dataStore.data.map { it[stringPreferencesKey(key)] ?: default }
	}

	override fun getStringSet(key: String, default: Set<String>?): Flow<Set<String>?> {
		return context.dataStore.data.map { it[stringSetPreferencesKey(key)] ?: default }
	}

	override suspend fun putInt(key: String, value: Int) {
		context.dataStore.edit { preferences -> preferences.clear() }
		context.dataStore.edit { preferences ->
			preferences[intPreferencesKey(key)] = value
		}
	}

	override suspend fun putLong(key: String, value: Long) {
		context.dataStore.edit { preferences ->
			preferences[longPreferencesKey(key)] = value
		}
	}

	override suspend fun putFloat(key: String, value: Float) {
		context.dataStore.edit { preferences ->
			preferences[floatPreferencesKey(key)] = value
		}
	}

	override suspend fun putBoolean(key: String, value: Boolean) {
		context.dataStore.edit { preferences ->
			preferences[booleanPreferencesKey(key)] = value
		}
	}

	override suspend fun putString(key: String, value: String) {
		context.dataStore.edit { preferences ->
			preferences[stringPreferencesKey(key)] = value
		}
	}

	override suspend fun putStringSet(key: String, value: Set<String>) {
		context.dataStore.edit { preferences ->
			preferences[stringSetPreferencesKey(key)] = value
		}
	}

	override suspend fun removeInt(key: String) {
		context.dataStore.edit { preferences ->
			preferences.remove(intPreferencesKey(key))
		}
	}

	override suspend fun removeLong(key: String) {
		context.dataStore.edit { preferences ->
			preferences.remove(longPreferencesKey(key))
		}
	}

	override suspend fun removeFloat(key: String) {
		context.dataStore.edit { preferences ->
			preferences.remove(floatPreferencesKey(key))
		}
	}

	override suspend fun removeBoolean(key: String) {
		context.dataStore.edit { preferences ->
			preferences.remove(booleanPreferencesKey(key))
		}
	}

	override suspend fun removeString(key: String) {
		context.dataStore.edit { preferences ->
			preferences.remove(stringPreferencesKey(key))
		}
	}

	override suspend fun removeStringSet(key: String) {
		context.dataStore.edit { preferences ->
			preferences.remove(stringSetPreferencesKey(key))
		}
	}

	override suspend fun clearAll() {
		context.dataStore.edit { preferences ->
			preferences.clear()
		}
	}
}
