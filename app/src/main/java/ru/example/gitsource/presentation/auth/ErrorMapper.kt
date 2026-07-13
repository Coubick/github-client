package ru.example.gitsource.presentation.auth

import android.content.Context
import ru.example.gitsource.R
import ru.example.gitsource.data.network.NetworkError

object ErrorMapper {

    fun mapToStringMessage(context: Context, error: NetworkError): String {
        return when (error) {
            is NetworkError.OAuthError -> {
                context.getString(R.string.error_oauth)
            }

            is NetworkError.NetworkException -> {
                context.getString(R.string.error_network)
            }

            is NetworkError.EmptyResponseBody -> {
                context.getString(R.string.error_empty_response)
            }

            is NetworkError.Unauthorized -> {
                context.getString(R.string.error_unauthorized)
            }

            is NetworkError.NotFound -> {
                context.getString(R.string.error_not_found)
            }

            is NetworkError.BadRequest -> {
                context.getString(R.string.error_bad_request)
            }

            is NetworkError.ServerError -> {
                context.getString(R.string.error_server)
            }

            is NetworkError.Unknown -> {
                context.getString(R.string.error_unknown)
            }
        }
    }
}