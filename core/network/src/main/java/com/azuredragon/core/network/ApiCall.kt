package com.azuredragon.core.network

import com.azuredragon.core.common.DataState
import com.azuredragon.core.network.retrofit.RetrofitHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response

private const val TAG = "apiCall"

typealias NetworkCall<T> = suspend () -> Response<T>

suspend fun <T> RetrofitHttpClient.apiCall(
    apiTryCount: Int = 1,
    block: NetworkCall<T>,
): DataState<T> {
    var result: DataState<T> = DataState.InProgress

    suspend fun fetchData() {
        result = worker(block)
    }

    for (i in 1..apiTryCount) {
        fetchData()

        if (result is DataState.Error) {
            delay(500)
            logger.d(TAG, "API call retry count: $i, error: ${(result as DataState.Error).errorMessage}")
            fetchData()
        } else {
            break
        }
    }

    return result
}

private suspend fun <T> RetrofitHttpClient.worker(block: NetworkCall<T>): DataState<T> {
    return withContext(Dispatchers.IO) {
        return@withContext try {
            val response = block()

            val endPoint = response.raw().request.url.encodedPath

            if (response.raw().cacheResponse != null) {
                logger.d(TAG, "response from cache ${response.raw().request.url}")
            }

            if (response.raw().networkResponse != null) {
                logger.d(TAG, "response from network ${response.raw().request.url}")
            }

            processResponse(
                endPoint = endPoint,
                response = response,
            )
        } catch (e: Throwable) {
            logger.e(TAG, "error", e)

            // STOPSHIP: (akashkhunt) errorMsg should be derived from strings.xml
            val errorMessage = "Cannot Reach GetMega. Please check your connection and try again"

            DataState.Error(errorMessage, null)
        }
    }
}

fun <T> RetrofitHttpClient.processResponse(
    endPoint: String,
    response: Response<T>,
): DataState<T> {
    return if (response.isSuccessful) {
        val body = response.body()

        body?.let {
            logger.d(TAG, "completed. path=$endPoint")
            DataState.Success(it)
        } ?: run {
            // STOPSHIP: (akashkhunt) provide proper implementation of the following
            DataState.Error("", null)
        }
    } else {
        logger.e(TAG, response.message())

        try {
            val error = serializer.fromJson(response.errorBody()?.charStream().toString(), ApiError::class)
            logger.d(TAG, "api error endpoint=$endPoint error=$error")

            error.toDataStateError()
        } catch (t: Throwable) {
            logger.w(TAG, "error in parsing error response to ApiError", t)

            // STOPSHIP: (akashkhunt) errorMsg should be derived from strings.xml
            val errorMessage = "Something went wrong. Please try again later."

            // STOPSHIP: (akashkhunt) provide proper implementation of the following
            DataState.Error(errorMessage, null)
        }
    }
}