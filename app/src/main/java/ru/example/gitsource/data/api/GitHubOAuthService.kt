package ru.example.gitsource.data.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.example.gitsource.data.dto.AccessTokenResponse

internal interface GitHubOAuthService {

    @POST("login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") deviceCode: String,
        @Field("code") grantType: String
    ) : Response<AccessTokenResponse>
}