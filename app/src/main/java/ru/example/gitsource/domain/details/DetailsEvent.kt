package ru.example.gitsource.domain.details

internal sealed interface DetailsEvent {
    data object NavigateBack : DetailsEvent
    data class ShowError(val errorMessageId: Int) : DetailsEvent
}