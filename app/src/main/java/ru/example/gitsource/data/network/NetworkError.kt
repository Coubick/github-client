package ru.example.gitsource.data.network

import ru.example.gitsource.data.network.NetworkConstants.BAD_REQUEST
import ru.example.gitsource.data.network.NetworkConstants.EMPTY_RESPONSE_BODY
import ru.example.gitsource.data.network.NetworkConstants.NOT_FOUND
import ru.example.gitsource.data.network.NetworkConstants.SERVER_ERROR
import ru.example.gitsource.data.network.NetworkConstants.UNAUTHORIZED

internal sealed class NetworkError(message: String) : NetworkErrorHandler(message) {
    class Unauthorized(message: String = UNAUTHORIZED) : NetworkError(message)
    class NotFound(message: String = NOT_FOUND) : NetworkError(message)
    class ServerError(message: String = SERVER_ERROR) : NetworkError(message)
    class BadRequest(message: String = BAD_REQUEST) : NetworkError(message)
    class EmptyResponseBody(message: String = EMPTY_RESPONSE_BODY) : NetworkError(message)
    data class Unknown(override val message: String) : NetworkError(message)
}