package ru.example.gitsource.domain

import android.content.Context

internal interface OAuthLauncher {
    fun launchAuth(context: Context)
}