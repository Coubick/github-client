package ru.example.gitsource.presentation.search

import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal data class RepositorySearchState (
    val isLoading: Boolean,
    val isFound: Boolean,
    val repositoriesList: List<RepositoryCardEntity>,
    var searchRequestText: String = "",
)