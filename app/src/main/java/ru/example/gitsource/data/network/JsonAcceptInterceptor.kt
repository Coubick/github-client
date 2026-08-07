package ru.example.gitsource.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class  JsonAcceptInterceptor @Inject constructor() : Interceptor {
    private companion object {
        const val HEADER_NAME = "Accept"
        const val OAUTH_HEADER_VALUE = "application/json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val header = originalRequest
            .headers[HEADER_NAME]

        if (header == null){
            val newRequest = originalRequest
                .newBuilder()
                .header(HEADER_NAME, OAUTH_HEADER_VALUE)
                .build()

            return chain.proceed(newRequest)
        } else {
            return chain.proceed(originalRequest)
        }
    }
}