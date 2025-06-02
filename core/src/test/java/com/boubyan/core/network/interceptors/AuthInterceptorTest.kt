package com.boubyan.core.network.interceptors

import com.boubyan.core.network.NetworkConstants
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

class AuthInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var client: OkHttpClient
    private val testApiKey = "test-api-key-123"

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        authInterceptor = AuthInterceptor(testApiKey)
        client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test api key is added to request without query parameters`() {
        // Given
        mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .get()
            .build()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertTrue(response.isSuccessful)
        val recordedRequest = mockWebServer.takeRequest()
        val requestUrl = recordedRequest.requestUrl.toString()
        assertTrue("API key should be added to URL",
            requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$testApiKey"))
    }

    @Test
    fun `test api key is added to existing query parameters`() {
        // Given
        mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test?param1=value1&param2=value2"))
            .get()
            .build()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertTrue(response.isSuccessful)
        val recordedRequest = mockWebServer.takeRequest()
        val requestUrl = recordedRequest.requestUrl.toString()

        assertTrue("Original params should be preserved",
            requestUrl.contains("param1=value1"))
        assertTrue("Original params should be preserved",
            requestUrl.contains("param2=value2"))
        assertTrue("API key should be added",
            requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$testApiKey"))
    }



    @Test
    fun `test api key is added to request with empty query parameters`() {
        // Given
        mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test?"))
            .get()
            .build()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertTrue(response.isSuccessful)
        val recordedRequest = mockWebServer.takeRequest()
        val requestUrl = recordedRequest.requestUrl.toString()
        assertTrue("API key should be added",
            requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$testApiKey"))
    }

    @Test
    fun `test api key is added to POST request`() {
        // Given
        mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .post(okhttp3.RequestBody.create(null, "test body"))
            .build()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertTrue(response.isSuccessful)
        val recordedRequest = mockWebServer.takeRequest()
        val requestUrl = recordedRequest.requestUrl.toString()
        assertTrue("API key should be added to POST request",
            requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$testApiKey"))
        assertEquals("POST", recordedRequest.method)
    }

    @Test
    fun `test api key is added with special characters in url`() {
        // Given
        mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test?param=value%20with%20spaces"))
            .get()
            .build()

        // When
        val response = client.newCall(request).execute()

        // Then
        assertTrue(response.isSuccessful)
        val recordedRequest = mockWebServer.takeRequest()
        val requestUrl = recordedRequest.requestUrl.toString()

        assertTrue("Original encoded params should be preserved",
            requestUrl.contains("param=value%20with%20spaces"))
        assertTrue("API key should be added",
            requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$testApiKey"))
    }

    @Test
    fun `test multiple requests maintain api key consistency`() {
        // Given
        repeat(3) { mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200)) }

        val requests = listOf(
            Request.Builder().url(mockWebServer.url("/test1")).build(),
            Request.Builder().url(mockWebServer.url("/test2?param=value")).build(),
            Request.Builder().url(mockWebServer.url("/test3")).build()
        )

        // When & Then
        requests.forEach { request ->
            val response = client.newCall(request).execute()
            assertTrue(response.isSuccessful)

            val recordedRequest = mockWebServer.takeRequest()
            val requestUrl = recordedRequest.requestUrl.toString()
            assertTrue("Each request should have API key",
                requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$testApiKey"))
        }
    }

    @Test
    fun `test different api key values are handled correctly`() {
        // Given
        val differentApiKey = "different-api-key-456"
        val differentInterceptor = AuthInterceptor(differentApiKey)
        val differentClient = OkHttpClient.Builder()
            .addInterceptor(differentInterceptor)
            .build()

        mockWebServer.enqueue(MockResponse().setBody("success").setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .get()
            .build()

        // When
        val response = differentClient.newCall(request).execute()

        // Then
        assertTrue(response.isSuccessful)
        val recordedRequest = mockWebServer.takeRequest()
        val requestUrl = recordedRequest.requestUrl.toString()
        assertTrue("Should use the different API key",
            requestUrl.contains("${NetworkConstants.API_KEY_PARAM}=$differentApiKey"))
        assertFalse("Should not contain original API key",
            requestUrl.contains(testApiKey))
    }


}