package ru.example.gitsource.data.network

import okhttp3.Interceptor
import okhttp3.Response

class GitHubApiAcceptInterceptor : Interceptor {
    companion object {
        private const val HEADER_NAME = "Accept"
        private const val HEADER_VALUE = "application/vnd.github.v3+json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header(HEADER_NAME, HEADER_VALUE)
            .build()
        return chain.proceed(newRequest )
    }
}