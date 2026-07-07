package ru.example.gitsource.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.example.gitsource.data.network.NetworkError
import javax.inject.Inject

internal class AuthTokenInterceptor @Inject constructor(private val tokenManager: TokenManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            tokenManager.getToken()
        }

        if (!token.isNullOrEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)

        } else throw NetworkError.Unauthorized()
    }
}