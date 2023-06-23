package com.azuredragon.core.common

import com.azuredragon.core.common.serialization.JsonSerializerImpl
import com.azuredragon.core.common.serialization.Serializer
import org.koin.dsl.module

fun getCoreCommonModule(
	versionName: String,
	versionCode: String,
	flavor: String,
	isDebug: Boolean,
) = module {
	single {
		AppInfo(
			versionName = versionName,
			versionCode = versionCode,
			flavor = flavor,
			// STOPSHIP: (akashkhunt) Provide proper deviceInfo implementation
			deviceInfo = "NOT YET IMPLEMENTED",
		)
	}

	single { commonJson }

	single<Serializer> { JsonSerializerImpl(get()) }
}
