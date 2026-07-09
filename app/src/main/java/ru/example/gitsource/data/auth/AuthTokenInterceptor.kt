package ru.example.gitsource.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AuthTokenInterceptor @Inject constructor(private val tokenManager: TokenManager) :
    Interceptor {
    private companion object {
        const val HEADER_NAME = "Authorization"
        const val HEADER_VALUE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            tokenManager.getToken()
        }

        val newRequest = originalRequest
            .newBuilder()
            .addHeader(HEADER_NAME, "$HEADER_VALUE $token")
            .build()
        return chain.proceed(newRequest)

    }
}