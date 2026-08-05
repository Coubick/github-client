package ru.example.gitsource.presentation.search

import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal sealed interface RepositorySearchAction {
    data class RepositoryCardClicked(
        val repository: RepositoryCardEntity
    ) : RepositorySearchAction
    data object NavigateBack : RepositorySearchAction
    data class RepoNameEntered(
        val repoName: String
    ) : RepositorySearchAction
}