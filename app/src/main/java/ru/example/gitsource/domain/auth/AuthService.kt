package ru.example.gitsource.domain.auth

internal interface AuthService {
    suspend fun login(code: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
}