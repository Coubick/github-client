package ru.example.gitsource.presentation.details

internal sealed interface DetailsEvent {
    data object NavigateBack : DetailsEvent
    data class ShowError(val errorMessageId: Int) : DetailsEvent
}