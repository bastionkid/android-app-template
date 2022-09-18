package com.azuredragon.core.network.retrofit

import android.content.Context
import com.azuredragon.core.common.AppInfo
import com.azuredragon.core.common.DataState
import com.azuredragon.core.common.TAG
import com.azuredragon.core.common.serialization.Serializer
import com.azuredragon.core.log.Logger
import com.azuredragon.core.network.apiCall
import com.azuredragon.core.network.httpclient.HttpClient
import com.azuredragon.core.network.httpclient.NetworkTimeoutConfig
import com.azuredragon.core.network.httpclient.RequestType
import retrofit2.Response

class RetrofitHttpClient(
    val baseUrl: String,
    val logger: Logger,
    val networkTimeoutConfig: NetworkTimeoutConfig,
    val appInfo: AppInfo,
    val debug: Boolean,
    val context: Context,
    val serializer: Serializer,
): HttpClient {

    override suspend fun <Req, Res> makeRequest(
        requestBody: Req,
        endPoint: String,
        requestType: RequestType
    ): DataState<Res> {
        TODO("This should not be used with Retrofit as it uses Interface functions based API calls")
    }

    override fun clearApiCache() {
        try {
            okHttpClient.cache?.evictAll()
        } catch (t: Throwable) {
            logger.e(TAG, "error in clearing cache", t)
        }
    }
}