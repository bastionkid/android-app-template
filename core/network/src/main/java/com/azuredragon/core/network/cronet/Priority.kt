package com.azuredragon.core.network.cronet

/**
 * @see org.chromium.net.UrlRequest.Builder.setPriority
 */
enum class Priority {
	// Lowest request priority
	IDLE,

	// Very low request priority
	LOWEST,

	// Low request priority
	LOW,

	// Medium request priority
	MEDIUM,

	// Highest request priority
	HIGHEST,
}
