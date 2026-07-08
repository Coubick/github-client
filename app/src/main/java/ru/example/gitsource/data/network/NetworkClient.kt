package ru.example.gitsource.data.network

import retrofit2.Response
import retrofit2.Retrofit
import ru.example.gitsource.data.dto.AccessTokenResponse
import ru.example.gitsource.data.network.api.GitHubApi
import ru.example.gitsource.data.network.api.GitHubOAuthApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkClient @Inject constructor(
    retrofit: Retrofit
) {
    private val oauthService: GitHubOAuthApi by lazy {
        retrofit.create(GitHubOAuthApi::class.java)
    }

    private val gitHubApiService: GitHubApi by lazy {
        retrofit.create(GitHubApi::class.java)
    }

    suspend fun <T> execute(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()

            NetworkErrorHandler.handleResponse(response)?.let { networkError ->
                return Result.failure(networkError)
            }

            val body = response.body()
                ?: return Result.failure(NetworkError.EmptyResponseBody())

            Result.success(body)

        } catch (e: Exception) {
            Result.failure(NetworkError.Unknown(e.toString()))
        }
    }

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): Result<AccessTokenResponse> {
        return execute {
            oauthService.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                code = code
            )
        }
    }

    // TODO методы для API (будут реализовываться начиная с https://jira.rutube.ru/browse/MOBAPP-18310)
}