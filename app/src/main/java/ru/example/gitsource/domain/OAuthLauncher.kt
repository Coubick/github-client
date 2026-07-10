package ru.example.gitsource.domain

internal interface OAuthLauncher {
    fun launchAuth(clientId: String)
}