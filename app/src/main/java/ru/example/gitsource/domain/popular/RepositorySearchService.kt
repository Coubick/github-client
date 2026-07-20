package ru.example.gitsource.domain.popular

import ru.example.gitsource.data.dto.RepositoryDto

internal interface RepositorySearchService {
    suspend fun searchRepositories() : Result<List<RepositoryDto>>
}