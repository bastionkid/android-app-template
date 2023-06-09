package com.azuredragon.core.network.httpclient

import com.azuredragon.core.common.DataState

interface HttpClient {

	suspend fun <Req, Res> makeRequest(
		requestBody: Req,
		endPoint: String,
		requestType: RequestType,
	): DataState<Res>

	fun clearApiCache()
}
