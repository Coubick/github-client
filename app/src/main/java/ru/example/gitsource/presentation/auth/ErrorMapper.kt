package ru.example.gitsource.presentation.auth

import androidx.annotation.StringRes
import ru.example.gitsource.R
import ru.example.gitsource.data.network.NetworkError

object ErrorMapper {

    @StringRes
    fun mapToStringMessage(error: NetworkError): Int {
        return when (error) {
            is NetworkError.OAuthError -> R.string.error_oauth
            is NetworkError.NetworkException -> R.string.error_network
            is NetworkError.EmptyResponseBody -> R.string.error_empty_response
            is NetworkError.Unauthorized -> R.string.error_unauthorized
            is NetworkError.NotFound -> R.string.error_not_found
            is NetworkError.BadRequest -> R.string.error_bad_request
            is NetworkError.ServerError -> R.string.error_server
            is NetworkError.Unknown -> R.string.error_unknown
        }
    }
}