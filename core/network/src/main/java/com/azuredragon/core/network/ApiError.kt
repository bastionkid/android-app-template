package com.azuredragon.core.network

import com.azuredragon.core.common.DataState

@kotlinx.serialization.Serializable
class ApiError(
    val code: String,
    val description: String,
)

fun <T> ApiError.toDataStateError(): DataState<T> {
    return when (code) {
        else -> {
            DataState.Error("Cannot Reach GetMega. Please check your connection and try again", null)
        }
    }
}