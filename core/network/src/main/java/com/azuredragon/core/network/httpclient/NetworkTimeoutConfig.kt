package com.azuredragon.core.network.httpclient

class NetworkTimeoutConfig(
    val readTimeout: Long = 10,
    val connectTimeout: Long = 10,
    val writeTimeout: Long = 10,
)