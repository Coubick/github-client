package ru.example.gitsource.presentation.search

internal data class RepositorySearchScreenForm (
    val isLoading: Boolean,
    val isFound: Boolean,
    var searchRequestText: String,
)