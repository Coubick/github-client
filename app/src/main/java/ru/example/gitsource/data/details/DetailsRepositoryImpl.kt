package ru.example.gitsource.data.details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.example.gitsource.data.dto.toDetailsEntity
import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.api.GitHubApi
import ru.example.gitsource.domain.details.DetailsEntity
import ru.example.gitsource.domain.details.DetailsRepository
import javax.inject.Inject

internal class DetailsRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val networkClient: NetworkClient
) : DetailsRepository {
    override suspend fun getRepositoryDetails(
        ownerName: String,
        repoName: String
    ): Result<DetailsEntity> {
        return withContext(Dispatchers.IO) {
            networkClient.execute {
                gitHubApi.getRepository(
                    owner = ownerName,
                    repo = repoName
                )
            }.fold(
                onSuccess = { responseResult ->
                    Result.success(responseResult.toDetailsEntity())
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        }
    }
}