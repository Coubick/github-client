package ru.example.gitsource.data.dto

import com.google.gson.annotations.SerializedName
import ru.example.gitsource.domain.details.DetailsEntity

internal data class DetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("language") val language: String,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("description") val description: String?,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("open_issues_count") val openIssuesCount: Int
)

internal fun DetailsDto.toDetailsEntity(): DetailsEntity {
    return DetailsEntity(
        id = id,
        name = name,
        starsCount = stargazersCount,
        language = language,
        ownerName = owner.login,
        description = description,
        watchersCount = watchersCount,
        openIssuesCount = openIssuesCount,
    )
}