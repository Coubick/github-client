package ru.example.gitsource.data.common

import retrofit2.Response

internal sealed class NetworkErrorHandler(message: String) : Exception(message) {
    companion object {
        fun <T> handleResponse(response: Response<T>): NetworkError? {
            if (response.isSuccessful) return null

            return when (response.code()) {
                400 -> NetworkError.BadRequest()
                401 -> NetworkError.Unauthorized()
                404 -> NetworkError.NotFound()
                in 500..599 -> NetworkError.ServerError()
                else -> NetworkError.Unknown("${response.code()}")
            }
        }
    }
}