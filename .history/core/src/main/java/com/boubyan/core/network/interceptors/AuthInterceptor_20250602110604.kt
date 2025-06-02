package com.boubyan.core.network.interceptors
import com.boubyan.core.network.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * OkHttp interceptor for adding authentication parameters to requests.
 * Adds the API key as a query parameter to all requests.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    @Named("api_key") private val apiKey: String
) : Interceptor {

    /**
     * Intercepts the network call and adds the API key to the request URL.
     * Modifies the original request by adding the API key as a query parameter.
     *
     * @param chain The interceptor chain
     * @return The response from the chain with the modified request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(NetworkConstants.API_KEY_PARAM, apiKey)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}