package ru.example.gitsource.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.example.gitsource.data.network.NetworkError
import javax.inject.Inject

internal class AuthTokenInterceptor @Inject constructor(private val tokenManager: TokenManager) :
    Interceptor {
    private companion object {
        const val HEADER_NAME = "Authorization"
        const val HEADER_VALUE = "Bearer"

        const val OAUTH_HOST = "github.com"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            tokenManager.getToken()
        }

        if (!token.isNullOrEmpty() && originalRequest.url().host() != OAUTH_HOST) {
            val newRequest = originalRequest.newBuilder()
                .addHeader(HEADER_NAME, "$HEADER_VALUE $token")
                .build()
            return chain.proceed(newRequest)

        } else {
            throw NetworkError.Unauthorized()
        }
    }
}