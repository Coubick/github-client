package ru.example.gitsource.presentation

import android.content.Context
import androidx.annotation.StringRes
import ru.example.gitsource.R
import ru.example.gitsource.data.network.NetworkError

object ErrorMapper {

    fun map(error: Throwable): UiText = when (error) {
        is NetworkError.OAuthError -> UiText.Resource(
            R.string.error_oauth,
            listOf(error.description ?: error.error)
        )
        is NetworkError.NetworkException -> UiText.Resource(
            R.string.error_network,
            listOf(error.cause.localizedMessage.orEmpty())
        )
        is NetworkError.LoadingError -> UiText.Resource(
            R.string.loading_error,
            listOf(error.cause.localizedMessage.orEmpty())
        )
        is NetworkError.EmptyResponseBody -> UiText.Resource(R.string.error_empty_response)
        is NetworkError.Unauthorized -> UiText.Resource(R.string.error_unauthorized)
        is NetworkError.NotFound -> UiText.Resource(R.string.error_not_found)
        is NetworkError.BadRequest -> UiText.Resource(R.string.error_bad_request)
        is NetworkError.ServerError -> UiText.Resource(R.string.error_server)
        else -> UiText.Resource(R.string.error_unknown)
    }
}

sealed interface UiText {
    data class Resource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText

    fun asString(context: Context): String = when (this) {
        is Resource -> context.getString(
            resId,
            *args.map { if (it is UiText) it.asString(context) else it }.toTypedArray()
        )
    }
}