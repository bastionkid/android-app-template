package com.azuredragon.core.analytics

import com.azuredragon.core.analytics.provider.AnalyticsAgent

interface AppAnalytics {

	val analyticsAgents: MutableList<AnalyticsAgent>

	fun addAnalyticsProvider(analyticsAgent: AnalyticsAgent): Boolean

	fun removeAnalyticsProvider(analyticsAgent: AnalyticsAgent): Boolean

	fun setUserProperties(userId: String, properties: Map<String, Any?>)

	fun trackEvent(eventName: String, properties: Map<String, Any?>)

	fun onLogout()
}
