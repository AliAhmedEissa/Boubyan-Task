package com.boubyan.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.io.IOException

class LoggingInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var loggingInterceptor: LoggingInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request
    private lateinit var response: Response

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        loggingInterceptor = LoggingInterceptor()
        
        // Create a mock request with headers and body
        request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .post("""{"key": "value"}""".toRequestBody("application/json".toMediaType()))
            .build()

        // Create a mock response with headers and body
        response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .header("Content-Type", "application/json")
            .body("""{"response": "data"}""".toRequestBody("application/json".toMediaType()))
            .build()

        // Create a mock chain
        chain = mock(Interceptor.Chain::class.java)
        `when`(chain.request()).thenReturn(request)
        `when`(chain.proceed(request)).thenReturn(response)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test successful request and response logging`() {
        // When
        val result = loggingInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(200, result.code)
        assertEquals("""{"response": "data"}""", result.body?.string())
    }

    @Test(expected = IOException::class)
    fun `test network error logging`() {
        // Given
        `when`(chain.proceed(request)).thenThrow(IOException("Network error"))

        // When
        loggingInterceptor.intercept(chain)
    }

    @Test
    fun `test request with no body`() {
        // Given
        val requestWithoutBody = Request.Builder()
            .url(mockWebServer.url("/test"))
            .header("Content-Type", "application/json")
            .get()
            .build()
        `when`(chain.request()).thenReturn(requestWithoutBody)

        // When
        val result = loggingInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(200, result.code)
    }

    @Test
    fun `test response with no body`() {
        // Given
        val responseWithoutBody = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(204)
            .message("No Content")
            .build()
        `when`(chain.proceed(request)).thenReturn(responseWithoutBody)

        // When
        val result = loggingInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(204, result.code)
    }

    @Test
    fun `test request with query parameters`() {
        // Given
        val requestWithQuery = Request.Builder()
            .url(mockWebServer.url("/test?param1=value1&param2=value2"))
            .get()
            .build()
        `when`(chain.request()).thenReturn(requestWithQuery)

        // When
        val result = loggingInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(200, result.code)
    }

    @Test
    fun `test response with error status code`() {
        // Given
        val errorResponse = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(400)
            .message("Bad Request")
            .body("""{"error": "Invalid request"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(errorResponse)

        // When
        val result = loggingInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(400, result.code)
        assertEquals("""{"error": "Invalid request"}""", result.body?.string())
    }
} 