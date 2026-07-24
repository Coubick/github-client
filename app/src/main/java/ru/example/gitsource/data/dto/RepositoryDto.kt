package ru.example.gitsource.data.dto

import com.google.gson.annotations.SerializedName
import ru.example.gitsource.domain.popular.RepositoryEntity

internal data class RepositoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("language") val language: String,
    @SerializedName("description") val description: String,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("open_issues_count") val openIssuesCount: Int,
    @SerializedName("owner") val owner: Owner
)

internal fun RepositoryDto.toRepositoryEntity(): RepositoryEntity {
    return RepositoryEntity(
        id = id,
        name = name,
        starsCount = stargazersCount,
        language = language,
        authorName = owner.login,
        description = description,
        watchersCount = watchersCount,
        openIssuesCount = openIssuesCount
    )
}

internal data class Owner(
    @SerializedName("login") val login: String
)