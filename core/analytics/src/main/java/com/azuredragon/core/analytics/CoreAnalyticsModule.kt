package com.azuredragon.core.analytics

import org.koin.dsl.module

fun getCoreAnalyticsModule(
	isDebug: Boolean,
) = module {
	single<AppAnalytics> {
		AppAnalyticsImpl(
			context = get(),
			settings = get(),
			logger = get(),
		)
	}
}
