package ru.example.gitsource.presentation.auth

import ru.example.gitsource.presentation.UiText

internal sealed interface AuthEvent {
    data object NavigateToPopular : AuthEvent
    data object NavigateToOAuthScreen : AuthEvent
    data class ShowError(val message: UiText) : AuthEvent
}