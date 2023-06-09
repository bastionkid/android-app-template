package com.azuredragon.core.common

import kotlinx.serialization.json.Json

val commonJson: Json = Json {
	// ignoreUnknownKeys allows unknown properties in the input JSON to be ignored
	ignoreUnknownKeys = true
	//  This was required to fix this issue https://github.com/Kotlin/kotlinx.serialization/issues/1450#issuecomment-841214332
	useAlternativeNames = false
	// Reference: https://blog.jetbrains.com/kotlin/2021/09/kotlinx-serialization-1-3-released/#excluding-nulls
	explicitNulls = false
	encodeDefaults = true
	useArrayPolymorphism = true
}
