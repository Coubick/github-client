package ru.example.gitsource.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

internal data class RepositorySearchResponse (
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<RepositoryDto>
)
