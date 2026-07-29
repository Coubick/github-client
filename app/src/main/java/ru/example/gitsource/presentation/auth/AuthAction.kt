package ru.example.gitsource.presentation.auth

internal sealed interface AuthAction {
    data object LoginClicked : AuthAction
    data object LoginCancelled : AuthAction
}