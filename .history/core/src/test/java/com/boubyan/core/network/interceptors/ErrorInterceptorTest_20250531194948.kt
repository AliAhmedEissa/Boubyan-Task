package com.boubyan.core.network.interceptors

import com.boubyan.core.error.AppException
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.IOException

class ErrorInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var errorInterceptor: ErrorInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        errorInterceptor = ErrorInterceptor()
        
        // Create a mock request
        request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .header("Content-Type", "application/json")
            .post("""{"key": "value"}""".toRequestBody("application/json".toMediaType()))
            .build()

        // Create a mock chain
        chain = mock(Interceptor.Chain::class.java)
        `when`(chain.request()).thenReturn(request)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test successful response`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        val result = errorInterceptor.intercept(chain)

        // Then
        assertNotNull(result)
        assertEquals(200, result.code)
    }

    @Test(expected = AppException.UnauthorizedException::class)
    fun `test unauthorized error`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .body("""{"error": "Invalid credentials"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        errorInterceptor.intercept(chain)
    }

    @Test(expected = AppException.ForbiddenException::class)
    fun `test forbidden error`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(403)
            .message("Forbidden")
            .body("""{"error": "Access denied"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        errorInterceptor.intercept(chain)
    }

    @Test(expected = AppException.NotFoundException::class)
    fun `test not found error`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(404)
            .message("Not Found")
            .body("""{"error": "Resource not found"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        errorInterceptor.intercept(chain)
    }

    @Test(expected = AppException.RateLimitException::class)
    fun `test rate limit error`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(429)
            .message("Too Many Requests")
            .body("""{"error": "Rate limit exceeded"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        errorInterceptor.intercept(chain)
    }

    @Test(expected = AppException.ServerException::class)
    fun `test server error`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(500)
            .message("Internal Server Error")
            .body("""{"error": "Server error"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        errorInterceptor.intercept(chain)
    }

    @Test(expected = AppException.NetworkException::class)
    fun `test network error`() {
        // Given
        `when`(chain.proceed(request)).thenThrow(IOException("Network error"))

        // When
        errorInterceptor.intercept(chain)
    }

    @Test(expected = AppException.UnknownException::class)
    fun `test unknown error`() {
        // Given
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(418)
            .message("I'm a teapot")
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        // When
        errorInterceptor.intercept(chain)
    }

    @Test
    fun `test error response with custom error message`() {
        // Given
        val errorMessage = "Custom error message"
        val response = Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(400)
            .message("Bad Request")
            .body("""{"error": "$errorMessage"}""".toRequestBody("application/json".toMediaType()))
            .build()
        `when`(chain.proceed(request)).thenReturn(response)

        try {
            // When
            errorInterceptor.intercept(chain)
        } catch (e: AppException) {
            // Then
            assertEquals(errorMessage, e.message)
        }
    }
} 