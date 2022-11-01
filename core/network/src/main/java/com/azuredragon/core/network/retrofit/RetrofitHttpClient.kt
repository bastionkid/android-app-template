package com.azuredragon.core.network.retrofit

import android.content.Context
import com.azuredragon.core.common.AppInfo
import com.azuredragon.core.common.DataState
import com.azuredragon.core.common.TAG
import com.azuredragon.core.common.commonJson
import com.azuredragon.core.common.serialization.Serializer
import com.azuredragon.core.log.Logger
import com.azuredragon.core.network.cronet.CronetModule
import com.azuredragon.core.network.cronet.CronetOkHttpInterceptor
import com.azuredragon.core.network.httpclient.HttpClient
import com.azuredragon.core.network.httpclient.NetworkTimeoutConfig
import com.azuredragon.core.network.httpclient.RequestType
import com.azuredragon.core.network.interceptor.API_TOKEN_HEADER
import com.azuredragon.core.network.interceptor.AuthenticationInterceptor
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class RetrofitHttpClient(
    private val baseUrl: String,
    val logger: Logger,
    private val networkTimeoutConfig: NetworkTimeoutConfig,
    private val appInfo: AppInfo,
    private val isDebug: Boolean,
    private val applicationContext: Context,
    private val cache: Cache,
    private val cronetModule: CronetModule,
    val serializer: Serializer,
    val getToken: suspend () -> String?,
): HttpClient {

    override suspend fun <Req, Res> makeRequest(
        requestBody: Req,
        endPoint: String,
        requestType: RequestType
    ): DataState<Res> {
        TODO("This should not be used with Retrofit as it uses Interface functions based API calls")
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(networkTimeoutConfig.readTimeout, TimeUnit.SECONDS)
            .connectTimeout(networkTimeoutConfig.connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(networkTimeoutConfig.writeTimeout, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(AuthenticationInterceptor(logger, appInfo) { getToken() })
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(
                ChuckerInterceptor.Builder(applicationContext)
                    .collector(ChuckerCollector(applicationContext))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            // Cronet MUST be last & DO NOT ADD NetworkInterceptors
            // network interceptors will not be called because of Cronet. OkHttp will
            // not actually be used for networking -> but only as the networking api.
            .addInterceptor(cronetInterceptor)
            .build()
    }

    private val httpLoggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(
            if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        ).also {
            if (!isDebug) {
                it.redactHeader(API_TOKEN_HEADER)
            }
        }
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(commonJson.asConverterFactory("application/json".toMediaType()))
            .build().also {
                logger.d(TAG, "created retrofit instance. baseUrl=$baseUrl")
            }
    }

    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }

    private val cronetInterceptor: CronetOkHttpInterceptor by lazy {
        CronetOkHttpInterceptor(
            cronetModule = cronetModule,
            logger = logger,
        )
    }

    override fun clearApiCache() {
        try {
            okHttpClient.cache?.evictAll()
        } catch (t: Throwable) {
            logger.e(TAG, "error in clearing cache", t)
        }
    }
}