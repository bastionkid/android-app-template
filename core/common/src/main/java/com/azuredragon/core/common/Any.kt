package com.azuredragon.core.common

val Any.TAG: String
	get() = this::class.java.canonicalName ?: this::class.java.simpleName
