package com.azuredragon.core.network.retrofit

import kotlinx.coroutines.runBlocking
import com.azuredragon.core.common.AppInfo
import com.azuredragon.core.common.TAG
import com.azuredragon.core.log.Logger
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(
    private val logger: Logger,
    private val appInfo: AppInfo,
    private val tokenProvider: suspend () -> String?,
) : Interceptor {

    private val tokenScheme = "Bearer"

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = runBlocking {
            tokenProvider()
        }

        if (token == null) {
            logger.w(TAG, "could not retrieve token")
        }

        val headers = request.headers.newBuilder()
            .add("Authorization", "$tokenScheme ${token ?: ""}")
            .add("x-app-version-code", appInfo.versionCode)
            .add("x-app-flavor", appInfo.flavor)
            .add("x-app-version-name", appInfo.versionName)
            .addUnsafeNonAscii("x-device-info", appInfo.deviceInfo)
            .build()

        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}