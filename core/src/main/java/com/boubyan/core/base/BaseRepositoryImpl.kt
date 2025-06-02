package com.boubyan.core.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * Base implementation of the BaseRepository interface.
 * Provides common functionality for handling API calls and error handling.
 */
abstract class BaseRepositoryImpl : BaseRepository {
    
    /**
     * Safely executes an API call and wraps the result in a Resource.
     * Handles success and error cases, including null response bodies.
     *
     * @param apiCall The suspend function that makes the API call
     * @return A Flow of Resource containing the API response or error
     */
    protected fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(Resource.Success(data))
                } ?: emit(Resource.Error(Exception("Response body is null")))
            } else {
                emit(Resource.Error(Exception("API Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
    
    /**
     * Safely executes an API call, maps the response, and wraps the result in a Resource.
     * Includes a delay for testing purposes.
     *
     * @param apiCall The suspend function that makes the API call
     * @param mapper The function to map the API response to the desired type
     * @return A Flow of Resource containing the mapped response or error
     */
    protected fun <T, R> safeApiCallWithMapping(
        apiCall: suspend () -> Response<T>,
        mapper: (T) -> R
    ): Flow<Resource<R>> = flow {
        emit(Resource.Loading)
        delay(1000) // delay for 1 second to show loading (testing purposes)
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    val mappedData = mapper(data)
                    emit(Resource.Success(mappedData))
                } ?: emit(Resource.Error(Exception("Response body is null")))
            } else {
                emit(Resource.Error(Exception("API Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}