package ru.example.gitsource.data.popular

import ru.example.gitsource.data.dto.RepositoryDto
import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.data.network.api.GitHubApi
import ru.example.gitsource.domain.popular.RepositorySearchService
import javax.inject.Inject

internal class RepositorySearchServiceImpl @Inject constructor(
    private val githubApi: GitHubApi,
    private val networkClient: NetworkClient
) : RepositorySearchService {
    override suspend fun searchRepositories(): Result<List<RepositoryDto>> {
        return networkClient.execute {
            githubApi.getRepositoriesList()
        }.fold(
            onSuccess = { responseResult ->
                val repositoriesList = responseResult.items
                if (repositoriesList.isEmpty()) {
                    Result.failure(NetworkError.Unknown)
                } else {
                    Result.success(repositoriesList)
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}