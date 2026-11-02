package ru.example.gitsource.presentation.details

import ru.example.gitsource.presentation.UiText

internal sealed interface DetailsEvent {
    data object NavigateBack : DetailsEvent
    data class ShowError(val message: UiText) : DetailsEvent
}