package me.aleksandarzekovic.labsearchwithpaging3.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.paging.PagingSource
import androidx.room.OnConflictStrategy
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Movie>)

    @Query("SELECT * FROM movies ORDER BY page")
    fun getMovies(): PagingSource<Int, Movie>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}
