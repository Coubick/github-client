package ru.example.gitsource.domain.auth

internal sealed interface AuthAction {
    data object LoginClicked : AuthAction
    data object LoginCancelled : AuthAction
}