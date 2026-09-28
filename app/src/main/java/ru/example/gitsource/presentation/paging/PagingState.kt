package ru.example.gitsource.presentation.paging

internal data class PagingState<T>(
    val items: List<T>,
    val isLoading: Boolean,
    val error: Throwable?,
    val endOfPaginationReached: Boolean
)