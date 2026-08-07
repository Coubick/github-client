package ru.example.gitsource.data.network

import ru.example.gitsource.BuildConfig

internal object NetworkConstants {
    const val OAUTH_URL = "https://github.com/login/oauth/access_token"
    const val BASE_API_URL = "https://api.github.com/"
    const val REDIRECT_URI = "gitsource://oauth"
    const val AUTHORIZE_URL = "https://github.com/login/oauth/authorize"
    const val HEADER_ACCEPT_VALUE = "application/vnd.github.v3+json"
    const val HEADER_ACCEPT = "Accept"
    const val GITHUB_CLIENT_ID = BuildConfig.GITHUB_CLIENT_ID
    const val GITHUB_CLIENT_SECRET = BuildConfig.GITHUB_CLIENT_SECRET
    const val RETRY_DELAY = 500L
    const val MAX_RETRIES = 5
}