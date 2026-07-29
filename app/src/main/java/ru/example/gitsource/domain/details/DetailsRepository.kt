package ru.example.gitsource.domain.details

internal interface DetailsRepository {
    suspend fun getRepositoryDetails(ownerName: String, repoName: String) : Result<DetailsEntity>
}