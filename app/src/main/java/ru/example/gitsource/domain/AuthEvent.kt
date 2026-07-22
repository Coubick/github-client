package ru.example.gitsource.domain

internal sealed interface AuthEvent {
    data object LaunchAuth : AuthEvent
    data object NavigateToPopular : AuthEvent
    data class ShowError(val errorMessageResId: Int) : AuthEvent
}