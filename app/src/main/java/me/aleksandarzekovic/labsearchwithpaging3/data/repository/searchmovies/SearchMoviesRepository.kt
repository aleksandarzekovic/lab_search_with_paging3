package me.aleksandarzekovic.labsearchwithpaging3.data.repository.searchmovies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.aleksandarzekovic.labsearchwithpaging3.data.api.TmdbService
import me.aleksandarzekovic.labsearchwithpaging3.data.local.TmdbDatabase
import me.aleksandarzekovic.labsearchwithpaging3.data.mediator.searchmovies.SearchMoviesMediator
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie
import javax.inject.Inject

interface SearchMoviesRepository {

    fun searchByQuery(query: String): Flow<PagingData<Movie>>
}

class SearchMoviesRepositoryImpl @Inject constructor(
    private val service: TmdbService,
    private val database: TmdbDatabase
) : SearchMoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun searchByQuery(query: String) = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = SearchMoviesMediator(
            query,
            service,
            database
        ),
        pagingSourceFactory = { database.moviesDao().getMovies() }
    ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}