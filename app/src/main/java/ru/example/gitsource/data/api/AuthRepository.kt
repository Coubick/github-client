package ru.example.gitsource.data.api

internal interface AuthRepository {
    suspend fun login(clientId: String, clientSecret: String, code: String) : Result<Unit>
    suspend fun logout() : Result<Unit>
}