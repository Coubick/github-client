package ru.example.gitsource.domain.popular

internal interface RepoRepository {
    suspend fun getRepositories() : Result<List<RepositoryCardEntity>>

    suspend fun getRepositoriesByName(repositoryName: String) : Result<List<RepositoryCardEntity>>
}