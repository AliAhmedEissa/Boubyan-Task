package com.boubyan.core.base

/**
 * Sealed class representing a resource state in the application.
 * Can be either a Success with data, an Error with exception,
 * or a Loading state.
 */
sealed class Resource<out T> {
    /** Represents a successful operation with data */
    data class Success<out T>(val data: T) : Resource<T>()
    /** Represents an error state with the exception */
    data class Error(val exception: Throwable) : Resource<Nothing>()
    /** Represents a loading state */
    object Loading : Resource<Nothing>()
    
    /** Returns true if the resource is in loading state */
    val isLoading: Boolean
        get() = this is Loading
        
    /** Returns true if the resource is in success state */
    val isSuccess: Boolean
        get() = this is Success
        
    /** Returns true if the resource is in error state */
    val isError: Boolean
        get() = this is Error
}

/**
 * Extension function to handle successful resource states.
 * @param action The action to perform with the success data
 * @return The original Resource for chaining
 */
inline fun <T> Resource<T>.onSuccess(action: (value: T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

/**
 * Extension function to handle error resource states.
 * @param action The action to perform with the exception
 * @return The original Resource for chaining
 */
inline fun <T> Resource<T>.onError(action: (exception: Throwable) -> Unit): Resource<T> {
    if (this is Resource.Error) action(exception)
    return this
}

/**
 * Extension function to handle loading resource states.
 * @param action The action to perform during loading
 * @return The original Resource for chaining
 */
inline fun <T> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Resource.Loading) action()
    return this
}