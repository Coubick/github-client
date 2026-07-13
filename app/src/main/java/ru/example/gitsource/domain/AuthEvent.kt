package ru.example.gitsource.domain

import ru.example.gitsource.data.network.NetworkError

internal sealed interface AuthEvent {
    data object LaunchAuth : AuthEvent
    data object NavigateToPopular : AuthEvent
    data class ShowError(val error: NetworkError) : AuthEvent
}