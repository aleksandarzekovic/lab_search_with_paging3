package me.aleksandarzekovic.labsearchwithpaging3.data.model.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class MovieDTO constructor(
    @JsonNames("id") val id: Long,
    @JsonNames("title") val title: String?,
    @JsonNames("overview") val overview: String?,
    @JsonNames("popularity") val popularity: Float?,
    @JsonNames("poster_path") val posterPath: String?,
    @JsonNames("release_date") val releaseDate: String?,
    @JsonNames("vote_average") val voteAverage: Float?,
    @JsonNames("vote_count") val voteCount: Long?
)

fun MovieDTO.mapToEntity(page: Int) = Movie(
    id = id,
    title = title,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    page = page
)