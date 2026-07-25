package ru.example.gitsource.domain.popular

internal data class RepositoryCardEntity (
    val id: Int,
    val name: String,
    val starsCount: Int,
    val language: String?,
    val authorName: String,
)