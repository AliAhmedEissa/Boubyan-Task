package com.boubyan.core.network.interceptors

import android.util.Log
import com.boubyan.core.error.AppException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorInterceptor @Inject constructor() : Interceptor {
    private val TAG = "ErrorInterceptor"
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d(TAG, "Processing request: ${request.url}")
        
        return try {
            val response = chain.proceed(request)
            Log.d(TAG, "Response received with code: ${response.code}")
            
            when (response.code) {
                in 200..299 -> {
                    Log.d(TAG, "Request successful")
                    response
                }
                401 -> {
                    Log.e(TAG, "Unauthorized: Invalid API key")
                    throw AppException.UnauthorizedException("Invalid API key")
                }
                403 -> {
                    Log.e(TAG, "Forbidden: Access denied")
                    throw AppException.ForbiddenException("Access forbidden")
                }
                404 -> {
                    Log.e(TAG, "Not found: Resource not found")
                    throw AppException.NotFoundException("Resource not found")
                }
                429 -> {
                    Log.e(TAG, "Rate limit exceeded")
                    throw AppException.RateLimitException("Rate limit exceeded")
                }
                in 500..599 -> {
                    Log.e(TAG, "Server error: ${response.code}")
                    throw AppException.ServerException("Server error: ${response.code}")
                }
                else -> {
                    Log.e(TAG, "Unknown error: ${response.code}")
                    throw AppException.UnknownException("Unknown error: ${response.code}")
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}", e)
            throw AppException.NetworkException("Network error: ${e.message}")
        }
    }
}