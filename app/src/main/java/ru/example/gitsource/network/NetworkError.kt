package ru.example.gitsource.network

import ru.example.gitsource.common.NetworkConstants.BAD_REQUEST
import ru.example.gitsource.common.NetworkConstants.NOT_FOUND
import ru.example.gitsource.common.NetworkConstants.SERVER_ERROR
import ru.example.gitsource.common.NetworkConstants.UNAUTHORIZED

internal sealed class NetworkError(message: String) : NetworkErrorHandler(message) {
    class Unauthorized(message: String = UNAUTHORIZED) : NetworkError(message)
    class NotFound(message: String = NOT_FOUND) : NetworkError(message)
    class ServerError(message: String = SERVER_ERROR) : NetworkError(message)
    class BadRequest(message: String = BAD_REQUEST) : NetworkError(message)
    data class Unknown(override val message: String) : NetworkError(message)
}