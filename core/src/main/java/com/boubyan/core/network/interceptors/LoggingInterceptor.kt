package com.boubyan.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp interceptor for logging network requests and responses.
 * Uses HttpLoggingInterceptor internally with BODY level logging.
 */
@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {

    /** Internal HttpLoggingInterceptor instance configured for BODY level logging */
    private val httpLoggingInterceptor = HttpLoggingInterceptor { message -> }
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    /**
     * Intercepts the network call and logs the request/response details.
     *
     * @param chain The interceptor chain
     * @return The response from the chain
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        return httpLoggingInterceptor.intercept(chain)
    }
}