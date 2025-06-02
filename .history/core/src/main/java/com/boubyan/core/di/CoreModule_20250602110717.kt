package com.boubyan.core.di

import android.content.Context
import com.boubyan.core.error.ErrorHandler
import com.boubyan.core.error.ErrorMapper
import com.boubyan.core.network.ApiClient
import com.boubyan.core.network.interceptors.AuthInterceptor
import com.boubyan.core.network.interceptors.ErrorInterceptor
import com.boubyan.core.network.interceptors.LoggingInterceptor
import com.boubyan.core.security.CertificatePinner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides core dependencies for the application.
 * All dependencies are scoped to the application lifecycle using SingletonComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    /**
     * Provides an ErrorHandler instance for handling application errors.
     *
     * @param context The application context
     * @return An ErrorHandler instance
     */
    @Provides
    @Singleton
    fun provideErrorHandler(@ApplicationContext context: Context): ErrorHandler {
        return ErrorHandler(context)
    }

    /**
     * Provides an ErrorMapper instance for mapping errors to resources.
     *
     * @param errorHandler The ErrorHandler instance
     * @return An ErrorMapper instance
     */
    @Provides
    @Singleton
    fun provideErrorMapper(errorHandler: ErrorHandler): ErrorMapper {
        return ErrorMapper(errorHandler)
    }

    /**
     * Provides a CertificatePinner instance for SSL certificate pinning.
     *
     * @return A CertificatePinner instance
     */
    @Provides
    @Singleton
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner()
    }

    /**
     * Provides an OkHttpClient instance with all necessary interceptors.
     *
     * @param authInterceptor Interceptor for adding authentication
     * @param errorInterceptor Interceptor for handling errors
     * @param loggingInterceptor Interceptor for logging requests/responses
     * @param certificatePinner Certificate pinning configuration
     * @return A configured OkHttpClient instance
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor,
        loggingInterceptor: LoggingInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient {
        return ApiClient.createOkHttpClient(
            authInterceptor,
            errorInterceptor,
            loggingInterceptor,
            certificatePinner
        )
    }

    /**
     * Provides a Retrofit instance for making API calls.
     *
     * @param okHttpClient The OkHttpClient instance to use
     * @return A configured Retrofit instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return ApiClient.createRetrofit(okHttpClient)
    }
}