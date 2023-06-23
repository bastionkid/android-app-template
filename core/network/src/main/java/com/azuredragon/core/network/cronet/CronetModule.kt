package com.azuredragon.core.network.cronet

import android.content.Context
import com.azuredragon.core.common.TAG
import com.azuredragon.core.log.Logger
import com.google.android.gms.net.CronetProviderInstaller
import okhttp3.Request
import okio.Buffer
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest
import java.io.File
import java.util.concurrent.ExecutorService

/**
 * initializes the Cronet Engine
 *
 * Learn about Cronet:
 * https://developer.android.com/guide/topics/connectivity/cronet/start
 */
class CronetModule(
	private val executor: ExecutorService,
	private val logger: Logger,
) {
	/**
	 *  The engine instance
	 *
	 *  It's recommended to create only one instance of CronetEngine. A single instance can send multiple asynchronous requests.
	 *  Additionally, a storage directory doesn't support concurrent access by multiple CronetEngine instances (setStoragePath())
	 */
	internal var engine: CronetEngine? = null

	fun install(context: Context) {
		val installTask = CronetProviderInstaller.installProvider(context)
		installTask.addOnCompleteListener { task ->
			when {
				task.isSuccessful -> {
					logger.i(TAG, "CronetProvider installed. registering engine provider")

					buildEngineOnce(context)
				}
				else -> {
					logger.w(TAG, "Unable to load Cronet from Google play services")

					task.exception?.let { exception ->
						logger.w(TAG, "Error loading cronet", exception)
					}
				}
			}
		}
	}

	@Synchronized
	private fun buildEngineOnce(context: Context) {
		if (engine != null) return

		val cacheDir = File(context.cacheDir, "cronet-cache")
		if (!cacheDir.exists()) {
			cacheDir.mkdirs()
		}

		val engineBuilder = CronetEngine.Builder(context)
			.enableBrotli(true)
			.enableHttp2(true)
			.enableQuic(true)
			.setStoragePath(cacheDir.absolutePath)
			.enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISK, 10 * 1024 * 1024) // 10 MegaBytes

		engine = engineBuilder.build()
		logger.i(TAG, "Cronet Engine created")

		// TODO(akashkhunt): 18/09/22 enable this when Cronet is tested enough in production
		// URL.setURLStreamHandlerFactory(engine.createURLStreamHandlerFactory())
	}

	internal fun buildRequest(engine: CronetEngine, request: Request, callback: UrlRequest.Callback): UrlRequest {
		val requestBuilder = engine.newUrlRequestBuilder(
			request.url.toString(),
			callback,
			executor,
		)

		requestBuilder.setHttpMethod(request.method)

		// set headers
		val headers = request.headers
		var i = 0
		while (i < headers.size) {
			requestBuilder.addHeader(headers.name(i), headers.value(i))
			i += 1
		}

		// set body
		request.body?.let { requestBody ->
			requestBody.contentType()?.let { contentType ->
				requestBuilder.addHeader("Content-Type", contentType.toString())
			}

			val buffer = Buffer()
			requestBody.writeTo(buffer)
			requestBuilder.setUploadDataProvider(UploadDataProviders.create(buffer.readByteArray()), executor)
		}

		// set priority
		// if not priority is requested, MEDIUM is applied by default by Cronet
		val priority = request.tag(Priority::class.java)
		if (priority != null) {
			logger.d(TAG, "setting request priority to: $priority. url=${request.url}")
			requestBuilder.setPriority(priority.ordinal)
		}

		return requestBuilder.build()
	}
}
