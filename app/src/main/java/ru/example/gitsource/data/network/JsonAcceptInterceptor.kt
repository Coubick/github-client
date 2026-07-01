package ru.example.gitsource.data.network

import okhttp3.Interceptor
import okhttp3.Response

internal class JsonAcceptInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response? {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Accept", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}