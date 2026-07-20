package ru.example.gitsource.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.example.gitsource.data.dto.RepositorySearchResponse
import ru.example.gitsource.data.network.NetworkConstants.HEADER_ACCEPT
import ru.example.gitsource.data.network.NetworkConstants.HEADER_ACCEPT_VALUE

internal interface GitHubApi {
    private companion object {
        const val QUERY = "stars:>0"
        const val SORT_BY_PARAMETER = "stars"
        const val SORT_ORDER = "desc"
        const val PER_PAGE = 20
    }

    @GET("search/repositories")
    fun getRepositoriesList(
        @Header(HEADER_ACCEPT) accept: String = HEADER_ACCEPT_VALUE,
        @Query("q") query: String = QUERY,
        @Query("sort") sort: String = SORT_BY_PARAMETER,
        @Query("order") order: String = SORT_ORDER,
        @Query("per_page") perPage: Int = PER_PAGE
    ): Response<RepositorySearchResponse>
}