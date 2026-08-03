package ru.example.gitsource.presentation.details

internal sealed interface DetailsAction {
    data object NavigateBackClicked : DetailsAction
    data object GetDetails : DetailsAction
}