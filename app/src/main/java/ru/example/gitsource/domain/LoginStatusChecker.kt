package ru.example.gitsource.domain

import ru.example.gitsource.data.auth.TokenManager
import javax.inject.Inject

internal class LoginStatusChecker @Inject constructor(
    private val tokenManager: TokenManager
){

    suspend fun isLoggedIn(): Boolean {
        return tokenManager
            .getToken()
            .isNullOrEmpty()
            .not()
    }
}