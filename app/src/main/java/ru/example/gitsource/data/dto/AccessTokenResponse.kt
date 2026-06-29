package ru.example.gitsource.data.dto

import com.google.gson.annotations.SerializedName

internal class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("scope") val scope: String
)