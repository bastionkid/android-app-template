package com.azuredragon.core.localstorage.settings

import android.content.Context
import androidx.annotation.GuardedBy
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal fun preferencesDataStore(
	name: String,
	corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
	produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
	scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
): ReadOnlyProperty<Context, DataStore<Preferences>> {
	return PreferenceDataStoreSingletonDelegate(name, corruptionHandler, produceMigrations, scope)
}

internal class PreferenceDataStoreSingletonDelegate internal constructor(
	private val name: String,
	private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
	private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
	private val scope: CoroutineScope,
) : ReadOnlyProperty<Context, DataStore<Preferences>> {

	private val lock = Any()

	@GuardedBy("lock")
	@Volatile
	private var INSTANCE: DataStore<Preferences>? = null

	/**
	 * Gets the instance of the DataStore.
	 *
	 * @param thisRef must be an instance of [Context]
	 * @param property not used
	 */
	override fun getValue(thisRef: Context, property: KProperty<*>): DataStore<Preferences> {
		return INSTANCE ?: synchronized(lock) {
			if (INSTANCE == null) {
				val applicationContext = thisRef.applicationContext

				INSTANCE = PreferenceDataStoreFactory.create(
					corruptionHandler = corruptionHandler,
					migrations = produceMigrations(applicationContext),
					scope = scope,
				) {
					applicationContext.preferencesDataStoreFile(name)
				}
			}
			INSTANCE!!
		}
	}
}

internal fun Context.preferencesDataStoreFile(name: String): File =
	this.dataStoreFile("$name.preferences_pb")

internal fun Context.dataStoreFile(fileName: String): File =
	File(applicationContext.filesDir, "datastore/$fileName")
