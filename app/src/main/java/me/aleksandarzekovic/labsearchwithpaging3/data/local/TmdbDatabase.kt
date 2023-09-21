package me.aleksandarzekovic.labsearchwithpaging3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.aleksandarzekovic.labsearchwithpaging3.data.local.daos.MoviesDao
import me.aleksandarzekovic.labsearchwithpaging3.data.local.daos.RemoteKeysDao
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.RemoteKeys

private const val DB_VERSION = 1

@Database(
    entities = [Movie::class, RemoteKeys::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class TmdbDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
