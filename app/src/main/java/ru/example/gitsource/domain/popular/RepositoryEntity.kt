package ru.example.gitsource.domain.popular

internal data class RepositoryEntity (
    val name: String,
    val starsCount: Int,
    val language: String?,
    val authorName: String,
    val description: String?,
    val watchersCount: Int,
    val openIssuesCount: Int
)