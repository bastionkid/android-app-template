package com.azuredragon.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class DataState<out T> {
	object InProgress : DataState<Nothing>()

	class Success<T>(val data: T?) : DataState<T>()

	class Error<T>(
		val errorMessage: String,
		val error: Throwable? = null,
	) : DataState<T>()

	fun isEndResultState() = this is Success || this is Error

	companion object {
		fun <T> success(data: T): Success<T> {
			return Success(data = data)
		}

		fun <T> error(
			errorMessage: String,
			error: Throwable? = null,
		): Error<T> {
			return Error(
				errorMessage = errorMessage,
				error = error,
			)
		}
	}
}

typealias DataStateFlow<T> = Flow<DataState<T>>

fun <T> Flow<T>.asResult(): DataStateFlow<T> {
	return this
		.map<T, DataState<T>> {
			DataState.Success(it)
		}
		.onStart { emit(DataState.InProgress) }
		.catch { emit(DataState.Error(it.message ?: "", it)) }
}
