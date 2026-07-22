package ru.example.gitsource.domain

internal interface AuthRepository {
    suspend fun login(code: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
}