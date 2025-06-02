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

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideErrorHandler(@ApplicationContext context: Context): ErrorHandler {
        return ErrorHandler(context)
    }

    @Provides
    @Singleton
    fun provideErrorMapper(errorHandler: ErrorHandler): ErrorMapper {
        return ErrorMapper(errorHandler)
    }

    @Provides
    @Singleton
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner()
    }

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

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return ApiClient.createRetrofit(okHttpClient)
    }
}