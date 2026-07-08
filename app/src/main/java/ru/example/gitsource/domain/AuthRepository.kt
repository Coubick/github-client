package ru.example.gitsource.domain

internal interface AuthRepository {
    suspend fun login(clientId: String, clientSecret: String, code: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
}