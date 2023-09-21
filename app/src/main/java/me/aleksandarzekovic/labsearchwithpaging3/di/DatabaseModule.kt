package me.aleksandarzekovic.labsearchwithpaging3.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.aleksandarzekovic.labsearchwithpaging3.data.local.daos.MoviesDao
import me.aleksandarzekovic.labsearchwithpaging3.data.local.daos.RemoteKeysDao
import me.aleksandarzekovic.labsearchwithpaging3.data.local.TmdbDatabase
import javax.inject.Singleton

private const val TMDB_DB = "tmdb_db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideOobleeDatabase(@ApplicationContext context: Context): TmdbDatabase =
        Room.databaseBuilder(
            context,
            TmdbDatabase::class.java,
            TMDB_DB
        ).build()

    @Provides
    fun provideMoviesDao(tmdbDatabase: TmdbDatabase): MoviesDao = tmdbDatabase.moviesDao()

    @Provides
    fun provideRemoteKeysDao(tmdbDatabase: TmdbDatabase): RemoteKeysDao = tmdbDatabase.remoteKeysDao()

}