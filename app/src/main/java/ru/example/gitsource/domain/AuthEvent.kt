package ru.example.gitsource.domain

internal sealed interface AuthEvent {
    data class LaunchAuth(val clientId: String) : AuthEvent
    data object NavigateToPopular : AuthEvent
    data class ShowError(val error: String) : AuthEvent
}