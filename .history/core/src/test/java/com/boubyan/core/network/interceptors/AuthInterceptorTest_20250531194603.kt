package com.boubyan.core.network.interceptors

import com.boubyan.core.network.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import javax.inject.Named

class AuthInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request
    private lateinit var response: Response

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        authInterceptor = AuthInterceptor("test-api-key")
        
        // Create a mock request
        request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .get()
            .build()

        // Create a mock response
        response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()

        // Create a mock chain
        chain = mock(Interceptor.Chain::class.java)
        `when`(chain.request()).thenReturn(request)
        `when`(chain.proceed(any())).thenReturn(response)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test api key is added to request`() {
        // When
        val result = authInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(200, result.code)
        
        // Verify the API key was added to the URL
        val newRequest = result.request
        val url = newRequest.url.toString()
        assertTrue(url.contains("${NetworkConstants.API_KEY_PARAM}=test-api-key"))
    }

    @Test
    fun `test api key is added to existing query parameters`() {
        // Given
        val requestWithParams = Request.Builder()
            .url(mockWebServer.url("/test?param1=value1"))
            .get()
            .build()
        `when`(chain.request()).thenReturn(requestWithParams)

        // When
        val result = authInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        val url = result.request.url.toString()
        assertTrue(url.contains("param1=value1"))
        assertTrue(url.contains("${NetworkConstants.API_KEY_PARAM}=test-api-key"))
    }

    private fun <T> any(): T {
        return org.mockito.ArgumentMatchers.any()
    }
} 