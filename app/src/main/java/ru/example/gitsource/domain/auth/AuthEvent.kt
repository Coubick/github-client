package ru.example.gitsource.domain.auth

internal sealed interface AuthEvent {
    data object LaunchAuth : AuthEvent
    data object NavigateToPopular : AuthEvent
    data class ShowError(val errorMessageResId: Int) : AuthEvent
}