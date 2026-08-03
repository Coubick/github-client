package ru.example.gitsource.presentation.auth

internal sealed interface AuthEvent {
    data object NavigateToPopular : AuthEvent
    data object NavigateToOAuthScreen : AuthEvent
    data class ShowError(val errorMessageResId: Int) : AuthEvent
}