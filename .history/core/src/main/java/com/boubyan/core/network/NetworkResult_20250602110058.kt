package com.boubyan.core.network

/**
 * Sealed class representing the result of a network operation.
 * Can be either a Success with data, an Error with code and message,
 * or an Exception with the throwable.
 */
sealed class NetworkResult<out T> {
    /** Represents a successful network operation with data */
    data class Success<out T>(val data: T) : NetworkResult<T>()
    /** Represents a network error with error code and message */
    data class Error(val code: Int, val message: String?) : NetworkResult<Nothing>()
    /** Represents a network exception with the throwable */
    data class Exception(val e: Throwable) : NetworkResult<Nothing>()
}

/**
 * Extension function to handle successful network results.
 * @param action The action to perform with the success data
 * @return The original NetworkResult for chaining
 */
inline fun <T> NetworkResult<T>.onSuccess(action: (value: T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

/**
 * Extension function to handle network errors.
 * @param action The action to perform with the error code and message
 * @return The original NetworkResult for chaining
 */
inline fun <T> NetworkResult<T>.onError(action: (code: Int, message: String?) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) action(code, message)
    return this
}

/**
 * Extension function to handle network exceptions.
 * @param action The action to perform with the exception
 * @return The original NetworkResult for chaining
 */
inline fun <T> NetworkResult<T>.onException(action: (exception: Throwable) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Exception) action(e)
    return this
}