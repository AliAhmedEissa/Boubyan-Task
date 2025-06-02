package com.boubyan.core.network.interceptors
import android.util.Log
import com.boubyan.core.network.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @Named("api_key") private val apiKey: String
) : Interceptor {
    private val TAG = "AuthInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "Adding API key to request")
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(NetworkConstants.API_KEY_PARAM, apiKey)
            .build()

        Log.d(TAG, "Request URL: ${newUrl}")

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}