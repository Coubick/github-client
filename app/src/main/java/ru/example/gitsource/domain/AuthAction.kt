package ru.example.gitsource.domain

internal sealed interface AuthAction {
    data object LoginClicked : AuthAction
}