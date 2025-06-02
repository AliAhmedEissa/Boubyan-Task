package com.boubyan.core.network

import com.boubyan.core.network.interceptors.AuthInterceptor
import com.boubyan.core.network.interceptors.ErrorInterceptor
import com.boubyan.core.network.interceptors.LoggingInterceptor
import com.boubyan.core.security.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Factory object for creating network clients and API services.
 * Provides methods to create OkHttpClient and Retrofit instances with proper configuration.
 */
object ApiClient {
    
    /**
     * Creates an OkHttpClient instance with all necessary interceptors and configuration.
     *
     * @param authInterceptor Interceptor for adding authentication
     * @param errorInterceptor Interceptor for handling errors
     * @param loggingInterceptor Interceptor for logging requests/responses
     * @param certificatePinner Certificate pinning configuration
     * @return Configured OkHttpClient instance
     */
    fun createOkHttpClient(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor,
        loggingInterceptor: LoggingInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NetworkConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
            //.certificatePinner(certificatePinner.getCertificatePinner())
            .build()
    }
    
    /**
     * Creates a Retrofit instance with the provided OkHttpClient.
     *
     * @param okHttpClient The OkHttpClient instance to use
     * @return Configured Retrofit instance
     */
    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}