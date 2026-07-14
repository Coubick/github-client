package ru.example.gitsource.domain

import ru.example.gitsource.data.auth.TokenManager
import javax.inject.Inject

internal class CheckLoginStatus @Inject constructor(
    private val tokenManager: TokenManager
){

    suspend fun isLoggedIn(): Boolean {
        return !tokenManager.getToken().isNullOrEmpty()
    }
}