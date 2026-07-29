package ru.example.gitsource.domain.details

internal sealed interface DetailsAction {
    data object NavigateBackClicked : DetailsAction
}