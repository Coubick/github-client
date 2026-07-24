package ru.example.gitsource.domain.popular

internal interface RepositoryLoadService {
    suspend fun loadRepositories() : Result<List<RepositoryEntity>>
}