package ru.example.gitsource.data.network

sealed class NetworkError : Exception() {

    data class OAuthError(val error: String, val description: String?) : NetworkError()
    data class NetworkException(override val cause: Throwable) : NetworkError()
    data object EmptyResponseBody : NetworkError()
    data object Unauthorized : NetworkError()
    data object NotFound : NetworkError()
    data object BadRequest : NetworkError()
    data object ServerError : NetworkError()
    data object Unknown : NetworkError()
}