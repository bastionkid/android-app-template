package com.azuredragon.core.network.cronet

import com.azuredragon.core.log.Logger
import com.azuredragon.core.network.Injector
import okhttp3.Interceptor
import okhttp3.Response

class CronetOkHttpInterceptor(
    private val cronetModule: CronetModule,
    private val logger: Logger,
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return Injector.provideCronetEngine()?.let { engine ->
            val callback = CronetUrlRequestCallback(
                originalRequest = chain.request(),
                call = chain.call(),
                logger = logger,
            )

            val urlRequest = cronetModule.buildRequest(engine, chain.request(), callback)
            urlRequest.start()

            callback.waitForDone()
        } ?: run {
            chain.proceed(chain.request())
        }
    }
}
