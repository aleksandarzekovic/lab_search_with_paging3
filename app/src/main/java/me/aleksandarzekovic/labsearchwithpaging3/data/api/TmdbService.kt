package me.aleksandarzekovic.labsearchwithpaging3.data.api

import me.aleksandarzekovic.labsearchwithpaging3.data.response.SearchMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): SearchMoviesResponse
}
