package me.aleksandarzekovic.labsearchwithpaging3.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String?,
    val overview: String?,
    val popularity: Float?,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float?,
    @ColumnInfo(name = "vote_count")
    val voteCount: Long?,
    var page: Int? = null,
)