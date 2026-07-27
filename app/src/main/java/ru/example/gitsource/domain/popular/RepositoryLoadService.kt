package ru.example.gitsource.domain.popular

internal interface RepositoryLoadService {
    suspend fun getRepositories() : Result<List<RepositoryCardEntity>>
}