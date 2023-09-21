package com.azuredragon.core.analytics

import android.content.Context
import com.azuredragon.core.analytics.provider.AnalyticsAgent
import com.azuredragon.core.localstorage.settings.Settings
import com.azuredragon.core.log.Logger

class AppAnalyticsImpl(
	val context: Context,
	val settings: Settings,
	val logger: Logger,
) : AppAnalytics {

	override val analyticsAgents = mutableListOf<AnalyticsAgent>()

	override fun addAnalyticsProvider(analyticsAgent: AnalyticsAgent): Boolean {
		return analyticsAgents.add(analyticsAgent)
	}

	override fun removeAnalyticsProvider(analyticsAgent: AnalyticsAgent): Boolean {
		return analyticsAgents.remove(analyticsAgent)
	}

	override fun setUserProperties(userId: String, properties: Map<String, Any?>) {
		analyticsAgents.forEach { analyticsProvider ->
			analyticsProvider.setUserProperties(userId, properties)
		}
	}

	override fun trackEvent(eventName: String, properties: Map<String, Any?>) {
		analyticsAgents.forEach { analyticsProvider ->
			analyticsProvider.trackEvent(eventName, properties)
		}
	}

	override fun onLogout() {
		analyticsAgents.forEach { analyticsProvider ->
			analyticsProvider.onLogout()
		}
	}
}
