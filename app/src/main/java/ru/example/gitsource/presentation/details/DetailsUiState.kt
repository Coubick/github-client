package ru.example.gitsource.presentation.details

import ru.example.gitsource.domain.details.DetailsEntity

internal data class DetailsUiState(
    val isLoading: Boolean,
    val details: DetailsEntity?
)