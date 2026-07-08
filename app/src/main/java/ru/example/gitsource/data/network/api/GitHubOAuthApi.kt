package ru.example.gitsource.data.network.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.example.gitsource.data.dto.AccessTokenResponse
import ru.example.gitsource.data.network.NetworkConstants.OAUTH_URL

internal interface GitHubOAuthApi {
    @FormUrlEncoded
    @POST(OAUTH_URL)
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Response<AccessTokenResponse>
}