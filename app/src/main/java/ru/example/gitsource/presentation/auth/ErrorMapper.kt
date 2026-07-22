package ru.example.gitsource.presentation.auth

import androidx.annotation.StringRes
import ru.example.gitsource.R
import ru.example.gitsource.data.network.NetworkError

object ErrorMapper {

    @StringRes
    fun mapToStringMessage(networkError: Throwable): Int {
        val error = networkError as? NetworkError ?: NetworkError.Unknown
        return when (error) {
            is NetworkError.OAuthError -> R.string.error_oauth
            is NetworkError.NetworkException -> R.string.error_network
            is NetworkError.EmptyResponseBody -> R.string.error_empty_response
            is NetworkError.NotFound -> R.string.error_not_found
            is NetworkError.BadRequest -> R.string.error_bad_request
            is NetworkError.ServerError -> R.string.error_server
            is NetworkError.Unknown -> R.string.error_unknown
        }
    }
}