package me.aleksandarzekovic.labsearchwithpaging3.data.mediator.searchmovies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import me.aleksandarzekovic.labsearchwithpaging3.data.api.TmdbService
import me.aleksandarzekovic.labsearchwithpaging3.data.local.TmdbDatabase
import me.aleksandarzekovic.labsearchwithpaging3.data.model.dto.mapToEntity
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.RemoteKeys
import retrofit2.HttpException
import java.io.IOException

private const val TMDB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SearchMoviesMediator(
    private val query: String,
    private val service: TmdbService,
    private val tmdbDatabase: TmdbDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: TMDB_STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        val apiQuery = query

        return try {
            val movies = service.searchMovies(apiQuery, page).items

            val endOfPaginationReached = movies.isEmpty()

            tmdbDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    tmdbDatabase.remoteKeysDao().clearRemoteKeys()
                    tmdbDatabase.moviesDao().clearMovies()
                }

                val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = movies.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                tmdbDatabase.remoteKeysDao().insertAll(keys)
                tmdbDatabase.moviesDao().insertAll(movies.map { movie -> movie.mapToEntity(page) })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            tmdbDatabase.remoteKeysDao().remoteKeysMovieId(movie.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            tmdbDatabase.remoteKeysDao().remoteKeysMovieId(movie.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                tmdbDatabase.remoteKeysDao().remoteKeysMovieId(id)
            }
        }
    }
}