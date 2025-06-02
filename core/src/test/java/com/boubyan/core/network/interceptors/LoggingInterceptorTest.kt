package com.boubyan.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import io.mockk.every
import io.mockk.mockk
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl

class LoggingInterceptorTest {

    private lateinit var interceptor: LoggingInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request
    private lateinit var response: Response
    private lateinit var requestBody: RequestBody
    private lateinit var connection: Connection

    @Before
    fun setup() {
        // Initialize your interceptor
        interceptor = LoggingInterceptor()

        // Create mocks
        chain = mockk()
        request = mockk()
        response = mockk()
        requestBody = mockk()
        connection = mockk()

        // Set up stubs for HttpLoggingInterceptor
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response
        every { chain.connection() } returns connection
        every { connection.protocol() } returns Protocol.HTTP_1_1

        every { request.method } returns "GET"
        every { request.url } returns "https://example.com".toHttpUrl()
        every { request.headers } returns Headers.headersOf()
        every { request.body } returns requestBody

        every { requestBody.contentType() } returns null
        every { requestBody.contentLength() } returns 0L

        every { response.code } returns 200
        every { response.message } returns "OK"
        every { response.request } returns request
        every { response.protocol } returns Protocol.HTTP_1_1
        every { response.headers } returns Headers.headersOf()
        every { response.body } returns ResponseBody.create(null, "")
    }

}


