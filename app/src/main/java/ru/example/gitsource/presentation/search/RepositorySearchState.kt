package ru.example.gitsource.presentation.search

import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.paging.Paginator

internal data class RepositorySearchState (
    val isLoading: Boolean,
    val isFound: Boolean,
    var searchRequestText: String,
    val paginator: Paginator<RepositoryCardEntity>
)