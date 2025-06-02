package com.boubyan.core.network.interceptors

import io.mockk.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ErrorInterceptorTest {

    private lateinit var interceptor: ErrorInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request
    private lateinit var response: Response

    @Before
    fun setUp() {
        interceptor = ErrorInterceptor()
        chain = mockk()
        request = mockk()
        response = mockk()

        every { chain.request() } returns request
    }

    @Test
    fun `interceptor proceeds the chain and returns response`() {
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        verify(exactly = 1) { chain.request() }
        verify(exactly = 1) { chain.proceed(request) }
        assertEquals(response, result)
    }

    @Test(expected = IOException::class)
    fun `interceptor rethrows exceptions thrown by chain`() {
        every { chain.proceed(request) } throws IOException("Network error")

        interceptor.intercept(chain) // Should throw IOException
    }
}