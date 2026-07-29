package ru.example.gitsource.domain.auth

internal interface AuthRepository {
    suspend fun login(code: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
}