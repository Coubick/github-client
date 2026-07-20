package ru.example.gitsource.data.auth

import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.NetworkConstants
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.data.network.api.GitHubOAuthApi
import ru.example.gitsource.domain.auth.AuthService
import javax.inject.Inject

internal class AuthServiceImpl @Inject constructor(
    private val gitHubOAuthApi: GitHubOAuthApi,
    private val networkClient: NetworkClient,
    private val tokenManager: TokenManager
) : AuthService {

    override suspend fun login(
        code: String
    ): Result<Unit> {
        return networkClient.execute {
            gitHubOAuthApi.getAccessToken(
                clientId = NetworkConstants.GITHUB_CLIENT_ID,
                clientSecret = NetworkConstants.GITHUB_CLIENT_SECRET,
                code = code
            )
        }.fold(
            onSuccess = { responseResult ->
                val token = responseResult.accessToken
                if (token.isEmpty()) {
                    Result.failure(NetworkError.Unknown)
                } else {
                    tokenManager.saveToken(token)
                    Result.success(Unit)
                }
            },

            onFailure = { error ->
                Result.failure(error)
            }
        )
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