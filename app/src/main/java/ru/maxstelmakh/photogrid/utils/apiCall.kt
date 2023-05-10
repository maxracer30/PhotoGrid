package ru.maxstelmakh.photogrid.utils

import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T : Any> apiCall(
    dispatcherProvider: DispatcherProvider,
    request: suspend () -> Response<T>
): Result<T> {

    return try {
        val response = withContext(dispatcherProvider.io) { request.invoke() }
        if (response.isSuccessful) Result.Success(data = response.body()!!)
        else Result.Failure(statusCode = response.code())
    } catch (e: Exception) {
        Result.Failure(message = e.message)
    }
}