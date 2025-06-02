package com.boubyan.core.di

import android.content.Context
import com.boubyan.core.error.ErrorHandler
import com.boubyan.core.error.ErrorMapper
import com.boubyan.core.network.interceptors.AuthInterceptor
import com.boubyan.core.network.interceptors.ErrorInterceptor
import com.boubyan.core.network.interceptors.LoggingInterceptor
import com.boubyan.core.security.CertificatePinner
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit

class CoreModuleTest {

    private lateinit var context: Context
    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var errorInterceptor: ErrorInterceptor
    private lateinit var loggingInterceptor: LoggingInterceptor
    private lateinit var certificatePinner: CertificatePinner

    @Before
    fun setup() {
        context = mock(Context::class.java)
        authInterceptor = mock(AuthInterceptor::class.java)
        errorInterceptor = mock(ErrorInterceptor::class.java)
        loggingInterceptor = mock(LoggingInterceptor::class.java)
        certificatePinner = mock(CertificatePinner::class.java)

        // Mock static ApiClient methods if needed
        // Alternatively, you could refactor ApiClient to inject OkHttpClient for easier testability
    }

    @Test
    fun `provideErrorHandler returns non null`() {
        val errorHandler = CoreModule.provideErrorHandler(context)
        assertNotNull(errorHandler)
        assertTrue(errorHandler is ErrorHandler)
    }

    @Test
    fun `provideErrorMapper returns non null`() {
        val errorHandler = CoreModule.provideErrorHandler(context)
        val errorMapper = CoreModule.provideErrorMapper(errorHandler)
        assertNotNull(errorMapper)
        assertTrue(errorMapper is ErrorMapper)
    }

    @Test
    fun `provideCertificatePinner returns non null`() {
        val certPinner = CoreModule.provideCertificatePinner()
        assertNotNull(certPinner)
        assertTrue(certPinner is CertificatePinner)
    }

    @Test
    fun `provideOkHttpClient returns non null`() {
        // Since ApiClient.createOkHttpClient is a static call,
        // if it internally creates OkHttpClient, this test works as integration.
        val okHttpClient = CoreModule.provideOkHttpClient(
            authInterceptor,
            errorInterceptor,
            loggingInterceptor,
            certificatePinner
        )
        assertNotNull(okHttpClient)
        assertTrue(okHttpClient is OkHttpClient)
    }

    @Test
    fun `provideRetrofit returns non null`() {
        val okHttpClient = CoreModule.provideOkHttpClient(
            authInterceptor,
            errorInterceptor,
            loggingInterceptor,
            certificatePinner
        )
        val retrofit = CoreModule.provideRetrofit(okHttpClient)
        assertNotNull(retrofit)
        assertTrue(retrofit is Retrofit)
    }
}