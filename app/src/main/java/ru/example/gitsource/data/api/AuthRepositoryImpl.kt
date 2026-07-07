package ru.example.gitsource.data.api

import ru.example.gitsource.data.auth.TokenManager
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.data.network.NetworkErrorHandler
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authService: GitHubOAuthService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(
        clientId: String,
        clientSecret: String,
        code: String
    ): Result<Unit> {
        return try {
            val response = authService
                .getAccessToken(
                    clientId,
                    clientSecret,
                    code
                )

            NetworkErrorHandler.handleResponse(response)?.let { networkError ->
                throw networkError
            }

            val body = response.body() ?: return Result.failure(NetworkError.EmptyResponseBody())

            tokenManager.saveToken(body.accessToken)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            tokenManager.removeToken()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}