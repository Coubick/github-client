package ru.example.gitsource.data.dto

import com.google.gson.annotations.SerializedName
import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal data class RepositoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("language") val language: String,
    @SerializedName("owner") val owner: Owner
)

internal fun RepositoryDto.toRepositoryEntity(): RepositoryCardEntity {
    return RepositoryCardEntity(
        id = id,
        name = name,
        starsCount = stargazersCount,
        language = language,
        authorName = owner.login,
    )
}

internal data class Owner(
    @SerializedName("login") val login: String
)