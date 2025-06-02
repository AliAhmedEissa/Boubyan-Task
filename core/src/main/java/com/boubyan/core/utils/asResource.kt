package com.boubyan.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.boubyan.core.base.Resource
import com.boubyan.core.error.AppException

/**
 * Extension function to convert a Flow of data to a Flow of Resource.
 * Maps successful values to Resource.Success and errors to Resource.Error.
 */
fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this.map<T, Resource<T>> { Resource.Success(it) }
        .catch { emit(Resource.Error(it)) }
}

/**
 * Extension function to map a Flow of Resource to a different type.
 * Preserves the Resource wrapper while transforming the data.
 *
 * @param mapper The function to transform the data
 * @return A Flow of Resource with the transformed data
 */
fun <T, R> Flow<Resource<T>>.mapResource(mapper: (T) -> R): Flow<Resource<R>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(mapper(resource.data))
            is Resource.Error -> Resource.Error(resource.exception)
            is Resource.Loading -> Resource.Loading
        }
    }
}

/**
 * Extension function to provide a default empty string for null values.
 */
fun String?.orEmpty(): String = this ?: ""

/**
 * Extension function to provide a default value for null or blank strings.
 *
 * @param default The default value to use if the string is null or blank
 * @return The original string or the default value
 */
fun String?.orDefault(default: String): String = if (isNullOrBlank()) default else this

/**
 * Extension function to provide an empty list for null values.
 */
fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

/**
 * Extension function to provide an empty map for null values.
 */
fun <K, V> Map<K, V>?.orEmpty(): Map<K, V> = this ?: emptyMap()

/**
 * Extension function to convert a Throwable to an AppException.
 * If the throwable is already an AppException, returns it as is.
 * Otherwise, wraps it in an UnknownException.
 *
 * @return An AppException representing the error
 */
fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        else -> AppException.UnknownException(message ?: "Unknown error occurred")
    }
}