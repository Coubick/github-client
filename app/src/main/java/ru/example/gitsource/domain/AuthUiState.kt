package ru.example.gitsource.domain

internal data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val Success: Boolean = true
)