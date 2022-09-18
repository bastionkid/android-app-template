package com.azuredragon.core.network.retrofit

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import com.azuredragon.core.common.TAG
import com.azuredragon.core.network.Injector
import com.azuredragon.core.network.cronet.CronetModule
import com.azuredragon.core.network.cronet.CronetOkHttpInterceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal val RetrofitHttpClient.okHttpClient: OkHttpClient
    get() = OkHttpClient.Builder()
        .readTimeout(networkTimeoutConfig.readTimeout, TimeUnit.SECONDS)
        .connectTimeout(networkTimeoutConfig.connectTimeout, TimeUnit.SECONDS)
        .writeTimeout(networkTimeoutConfig.writeTimeout, TimeUnit.SECONDS)
        .also {
            Injector.provideRetrofitCache()?.let { cache ->
                it.cache(cache)
                logger.d(TAG, "enabling cache. size=${cache.maxSize()}")
            }
        }
        .addInterceptor(AuthenticationInterceptor(logger, appInfo) {
            // STOPSHIP: (akashkhunt) Add proper token fetching logic
            ""
        })
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
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

internal val RetrofitHttpClient.httpLoggingInterceptor: HttpLoggingInterceptor
    get() = HttpLoggingInterceptor().setLevel(
        if (debug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    ).also {
        if (!debug) {
            it.redactHeader("Authorization")
        }
    }

private val RetrofitHttpClient.retrofit: Retrofit
    get() = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build().also {
            logger.d(TAG, "created retrofit instance. baseUrl=$baseUrl")
        }

internal inline fun <reified T> RetrofitHttpClient.createService(): T {
    return retrofit.create(T::class.java)
}

internal val RetrofitHttpClient.cronetInterceptor: CronetOkHttpInterceptor
    get() = CronetOkHttpInterceptor(
        cronetModule = CronetModule(
            executor = Executors.newSingleThreadExecutor(),
            logger = logger,
        ),
        logger = logger,
    ) 