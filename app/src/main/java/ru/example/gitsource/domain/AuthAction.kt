package ru.example.gitsource.domain

internal sealed interface AuthAction {
    data object LoginClicked : AuthAction
    data class AuthCodeReceived(val code: String): AuthAction
}