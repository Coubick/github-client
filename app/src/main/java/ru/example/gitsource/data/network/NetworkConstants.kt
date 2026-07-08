package ru.example.gitsource.data.network

internal object NetworkConstants {
    const val OAUTH_URL = "https://github.com/login/oauth/access_token"
    const val BASE_API_URL = "https://api.github.com/"
    const val UNAUTHORIZED = "Не авторизован"
    const val NOT_FOUND = "Ресурс не найден"
    const val SERVER_ERROR = "Ошибка сервера"
    const val BAD_REQUEST = "Некорректный запрос"
    const val EMPTY_RESPONSE_BODY = "Тело ответа пустое"
}