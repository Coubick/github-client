package ru.example.gitsource.domain.details

internal data class DetailsUiState(
    val isLoading: Boolean,
    val details: DetailsEntity?
)