package com.azuredragon.core.network

import android.content.Context
import okhttp3.Cache
import org.chromium.net.CronetEngine
import java.io.File

// STOPSHIP: (akashkhunt) Remove this Injector implementation and use Koin
internal object Injector {
	var contextProvider: (() -> Context)? = null
	private var cacheDirProvider: (() -> File)? = null
	private var cronetEngineProvider: (() -> CronetEngine)? = null

	fun registerCacheDirProvider(dirProvider: () -> File) {
		cacheDirProvider = dirProvider
	}

	fun registerContextProvider(ctxProvider: () -> Context) {
		contextProvider = ctxProvider
	}

	fun provideRetrofitCache(): Cache? {
		return cacheDirProvider?.let {
			Cache(it().resolve("retrofit-cache"), 10 * 1024 * 1024) // 10MB)
		}
	}

	fun provideCoilImageCache(): Cache? {
		return cacheDirProvider?.let {
			Cache(it().resolve("coil-cache"), 100 * 1024 * 1024) // 100MB)
		}
	}

	fun provideCronetEngine(): CronetEngine? = cronetEngineProvider?.invoke()

	fun registerCronetEngineProvider(p: () -> CronetEngine) {
		cronetEngineProvider = p
	}
}
