package ru.example.gitsource.data.popular

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    private companion object {
        const val QUERY = "stars:>0"
    }

    override suspend fun getRepositories(page: Int): Result<List<RepositoryCardEntity>> {
        return withContext(Dispatchers.IO) {
            networkClient.execute {
                githubApi.getRepositoriesList(
                    query = QUERY,
                    page = page,
                )
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

    override suspend fun getRepositoriesByName(repositoryName: String, page: Int): Result<List<RepositoryCardEntity>> {
        return withContext(Dispatchers.IO) {
            networkClient.execute {
                githubApi.getRepositoriesList(
                    query = repositoryName,
                    page = page
                )
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
}