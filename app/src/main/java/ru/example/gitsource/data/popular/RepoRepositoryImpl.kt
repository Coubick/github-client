package ru.example.gitsource.data.popular

import ru.example.gitsource.data.dto.toRepositoryEntity
import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.data.network.api.GitHubApi
import ru.example.gitsource.domain.popular.RepoRepository
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import javax.inject.Inject

internal class RepoRepositoryImpl @Inject constructor(
    private val githubApi: GitHubApi,
    private val networkClient: NetworkClient
) : RepoRepository {

    override suspend fun getRepositories(): Result<List<RepositoryCardEntity>> {
        return networkClient.execute {
            githubApi.getRepositoriesList()
        }.fold(
            onSuccess = { responseResult ->
                val repositoriesDtoList = responseResult.items
                if (repositoriesDtoList.isEmpty()) {
                    Result.failure(NetworkError.EmptyResponseBody)
                } else {
                    val repositoriesEntityList =
                        repositoriesDtoList.map { repo -> repo.toRepositoryEntity() }
                    Result.success(repositoriesEntityList)
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}