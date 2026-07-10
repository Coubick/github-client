package ru.example.gitsource.data.network

internal object NetworkConstants {
    const val OAUTH_URL = "https://github.com/login/oauth/access_token"
    const val BASE_API_URL = "https://api.github.com/"
    const val REDIRECT_URI = "gitsource://oauth"
    const val AUTHORIZE_URL = "https://github.com/login/oauth/authorize"
}