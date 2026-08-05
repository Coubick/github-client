package ru.example.gitsource.domain.details

internal data class DetailsEntity (
    val id: Int,
    val name: String,
    val starsCount: Int,
    val language: String?,
    val ownerName: String,
    val description: String?,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val avatarUrl: String
)