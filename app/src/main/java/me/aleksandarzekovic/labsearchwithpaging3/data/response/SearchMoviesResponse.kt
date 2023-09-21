package me.aleksandarzekovic.labsearchwithpaging3.data.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import me.aleksandarzekovic.labsearchwithpaging3.data.model.dto.MovieDTO

@Serializable
data class SearchMoviesResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val page: Int,
    @JsonNames("results") val items: List<MovieDTO> = emptyList(),
    @JsonNames("total_pages") val totalPages: Int,
    @JsonNames("total_results") val totalResults: Int
)