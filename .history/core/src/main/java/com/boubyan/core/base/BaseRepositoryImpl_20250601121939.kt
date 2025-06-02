package com.boubyan.core.base

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

abstract class BaseRepositoryImpl : BaseRepository {
    
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