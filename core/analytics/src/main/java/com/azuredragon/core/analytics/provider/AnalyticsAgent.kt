package com.azuredragon.core.analytics.provider

interface AnalyticsAgent {

	fun setUserProperties(userId: String, properties: Map<String, Any?>)

	fun trackEvent(eventName: String, properties: Map<String, Any?>)

	fun onLogout()
}
