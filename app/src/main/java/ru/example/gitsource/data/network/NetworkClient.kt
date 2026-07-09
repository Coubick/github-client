package ru.example.gitsource.data.network

import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkClient @Inject constructor(
    private val retrofit: Retrofit
) {
    suspend fun <T> execute(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()

            NetworkErrorHandler.handleResponse(response)?.let { networkError ->
                return Result.failure(networkError)
            }

            val body = response.body()
                ?: return Result.failure(NetworkError.EmptyResponseBody)

            Result.success(body)

        } catch (e: Exception) {
            Result.failure(NetworkError.Unknown)
        }
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}