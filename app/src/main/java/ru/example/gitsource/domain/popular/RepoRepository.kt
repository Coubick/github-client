package ru.example.gitsource.domain.popular

internal interface RepoRepository {
    suspend fun getRepositories(page: Int) : Result<List<RepositoryCardEntity>>

    suspend fun getRepositoriesByName(repositoryName: String, page: Int = 1,) : Result<List<RepositoryCardEntity>>
}