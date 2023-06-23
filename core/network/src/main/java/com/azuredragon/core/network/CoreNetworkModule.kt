package com.azuredragon.core.network

import com.azuredragon.core.network.cronet.CronetModule
import com.azuredragon.core.network.httpclient.NetworkTimeoutConfig
import com.azuredragon.core.network.retrofit.RetrofitHttpClient
import okhttp3.Cache
import org.koin.dsl.module
import java.util.concurrent.Executors

fun getCoreNetworkModule(
    baseUrl: String,
    isDebug: Boolean,
    cache: Cache,
    getToken: suspend () -> String?,
    onRefreshToken: suspend () -> String?,
) = module {
    single {
        CronetModule(
            executor = Executors.newSingleThreadExecutor(),
            logger = get(),
        ).apply {
            install(get())
        }
    }

    single {
        RetrofitHttpClient(
            context = get(),
            baseUrl = baseUrl,
            logger = get(),
            json = get(),
            networkTimeoutConfig = NetworkTimeoutConfig(),
            appInfo = get(),
            isDebug = isDebug,
            applicationContext = get(),
            cache = cache,
            cronetModule = get(),
            serializer = get(),
            getToken = getToken,
            onRefreshToken = onRefreshToken,
        )
    }
}