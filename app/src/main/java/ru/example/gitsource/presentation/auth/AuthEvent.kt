package ru.example.gitsource.presentation.auth

internal sealed interface AuthEvent {
    data object LaunchAuth : AuthEvent
    data object NavigateToPopular : AuthEvent
    data class ShowError(val errorMessageResId: Int) : AuthEvent
}