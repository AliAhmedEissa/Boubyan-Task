package com.boubyan.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.boubyan.core.base.Resource
import com.boubyan.core.error.AppException

// Flow Extensions
fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this.map<T, Resource<T>> { Resource.Success(it) }
        .catch { emit(Resource.Error(it)) }
}

fun <T, R> Flow<Resource<T>>.mapResource(mapper: (T) -> R): Flow<Resource<R>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(mapper(resource.data))
            is Resource.Error -> Resource.Error(resource.exception)
            is Resource.Loading -> Resource.Loading
        }
    }
}


// String Extensions
fun String?.orEmpty(): String = this ?: ""

fun String?.orDefault(default: String): String = if (isNullOrBlank()) default else this

// Collection Extensions
fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

fun <K, V> Map<K, V>?.orEmpty(): Map<K, V> = this ?: emptyMap()

// Exception Extensions
fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        else -> AppException.UnknownException(message ?: "Unknown error occurred")
    }
}