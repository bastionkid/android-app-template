package com.azuredragon.core.network.interceptor

import com.azuredragon.core.common.AppInfo
import com.azuredragon.core.common.TAG
import com.azuredragon.core.log.Logger
import kotlinx.coroutines.runBlocking
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
            .apply {
                token?.let { add(API_TOKEN_HEADER, "$tokenScheme $token") }
            }
            .add(APP_VERSION_CODE_HEADER, appInfo.versionCode)
            .add(APP_FLAVOR_HEADER, appInfo.flavor)
            .add(APP_VERSION_NAME_HEADER, appInfo.versionName)
            .addUnsafeNonAscii(DEVICE_INFO_HEADER, appInfo.deviceInfo)
            .build()

        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}

const val API_TOKEN_HEADER = "Authorization"
const val APP_VERSION_CODE_HEADER = "x-app-version-code"
const val APP_FLAVOR_HEADER = "x-app-flavor"
const val APP_VERSION_NAME_HEADER = "x-app-version-name"
const val DEVICE_INFO_HEADER = "x-device-info"