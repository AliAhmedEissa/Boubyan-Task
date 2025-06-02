package com.boubyan.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp interceptor for handling network errors.
 * Currently only passes through the chain, but can be extended to handle
 * specific error cases or add custom error handling logic.
 */
@Singleton
class ErrorInterceptor @Inject constructor() : Interceptor {

    /**
     * Intercepts the network call and handles any errors that occur.
     * Currently only throws network failures, but can be extended to handle
     * other types of errors or add custom error handling logic.
     *
     * @param chain The interceptor chain
     * @return The response from the chain
     * @throws Exception Only for actual network failures
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            throw e // Only throw for actual network failures
        }
    }
}