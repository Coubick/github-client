package ru.example.gitsource.data.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.example.gitsource.data.common.NetworkConstants.GRANT_TYPE_DEVICE_CODE
import ru.example.gitsource.data.dto.AccessTokenResponse

internal interface GitHubOAuthService {

    @POST("login/oauth/access_token")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("device_code") deviceCode: String,
        @Field("grant_type") grantType: String = GRANT_TYPE_DEVICE_CODE
    ) : Response<AccessTokenResponse>
}