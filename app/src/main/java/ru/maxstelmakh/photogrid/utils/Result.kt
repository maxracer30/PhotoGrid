package ru.maxstelmakh.photogrid.utils

sealed class Result<T : Any> {
    class Success<T : Any>(val data: T) : Result<T>()
    class Failure<T : Any>(val statusCode: Int = 0, val message: String? = null) : Result<T>()
}