package ru.example.gitsource.data.auth

import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient,
    private val tokenManager: TokenManager
) : AuthRepository {

    private companion object {
        const val TOKEN_IS_NULL = "Токен не получен"
    }

    override suspend fun login(
        clientId: String,
        clientSecret: String,
        code: String
    ): Result<Unit> {
        return networkClient.getAccessToken(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code).fold(
            onSuccess = { responseResult ->
                val token = responseResult.accessToken
                if (token.isEmpty()) {
                    Result.failure(NetworkError.Unknown(TOKEN_IS_NULL))
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