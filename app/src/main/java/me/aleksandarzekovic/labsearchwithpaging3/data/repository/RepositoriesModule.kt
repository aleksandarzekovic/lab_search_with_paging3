package me.aleksandarzekovic.labsearchwithpaging3.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.aleksandarzekovic.labsearchwithpaging3.data.repository.searchmovies.SearchMoviesRepository
import me.aleksandarzekovic.labsearchwithpaging3.data.repository.searchmovies.SearchMoviesRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindSearchMoviesRepository(repository: SearchMoviesRepositoryImpl): SearchMoviesRepository
}