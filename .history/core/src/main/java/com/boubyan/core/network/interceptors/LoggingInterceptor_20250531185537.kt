package com.boubyan.core.network.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {

    private val TAG = "NetworkCall"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body

        val requestLog = StringBuilder()
        requestLog.append("Request URL: ${request.url}\n")
        requestLog.append("Request Method: ${request.method}\n")
        requestLog.append("Request Headers: ${request.headers}\n")

        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            requestLog.append("Request Body: ${buffer.readUtf8()}\n")
        }

        Log.d(TAG, "Request: $requestLog")

        val response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            Log.e(TAG, "Network call failed: ${e.message}")
            throw e
        }

        val responseBody = response.body
        val responseBodyString = responseBody?.string()

        val responseLog = StringBuilder()
        responseLog.append("Response Code: ${response.code}\n")
        responseLog.append("Response Headers: ${response.headers}\n")
        responseLog.append("Response Body: $responseBodyString\n")

        Log.d(TAG, "Response: $responseLog")

        return response.newBuilder()
            .body(responseBodyString?.let { ResponseBody.create(responseBody.contentType(), it) })
            .build()
    }
}