package com.azuredragon.core.localstorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefsImpl(private val context: Context): Prefs {

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
}