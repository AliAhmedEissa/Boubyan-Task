package com.boubyan.core.network.interceptors

import com.boubyan.core.error.AppException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorInterceptor @Inject constructor() : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        return try {
            val response = chain.proceed(request)
            
            when (response.code) {
                in 200..299 -> response
                401 -> throw AppException.UnauthorizedException("Invalid API key")
                403 -> throw AppException.ForbiddenException("Access forbidden")
                404 -> throw AppException.NotFoundException("Resource not found")
                429 -> throw AppException.RateLimitException("Rate limit exceeded")
                in 500..599 -> throw AppException.ServerException("Server error: ${response.code}")
                else -> throw AppException.UnknownException("Unknown error: ${response.code}")
            }
        } catch (e: IOException) {
            throw AppException.NetworkException("Network error: ${e.message}")
        }
    }
}