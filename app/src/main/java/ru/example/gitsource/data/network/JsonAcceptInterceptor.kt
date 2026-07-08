package ru.example.gitsource.data.network

import okhttp3.Interceptor
import okhttp3.Response

internal class JsonAcceptInterceptor : Interceptor {
    private companion object {
        const val HEADER_NAME = "Accept"
        const val OAUTH_HEADER_VALUE = "application/json"
        const val API_HOST = "api.github.com"
        const val API_HEADER_VALUE = "application/vnd.github.v3+json"
    }

    override fun intercept(chain: Interceptor.Chain): Response? {
        val originalRequest = chain.request()

        val headerValue = if (originalRequest.url().host() == API_HOST) {
            API_HEADER_VALUE
        } else {
            OAUTH_HEADER_VALUE
        }

        val newRequest = originalRequest.newBuilder()
            .header(HEADER_NAME, headerValue)
            .build()

        return chain.proceed(newRequest)
    }
}