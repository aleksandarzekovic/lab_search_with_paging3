package me.aleksandarzekovic.labsearchwithpaging3.ui.movies

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.aleksandarzekovic.labsearchwithpaging3.R
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie
import me.aleksandarzekovic.labsearchwithpaging3.databinding.ActivityMoviesBinding
import me.aleksandarzekovic.labsearchwithpaging3.data.remote.RemotePresentationState
import me.aleksandarzekovic.labsearchwithpaging3.data.remote.asRemotePresentationState
import me.aleksandarzekovic.labsearchwithpaging3.ui.movies.items.movie.MoviesAdapter
import me.aleksandarzekovic.labsearchwithpaging3.ui.movies.items.moviesload.MoviesLoadStateAdapter

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.movies)

        binding.bindState(
            moviesUiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
    }

    private fun ActivityMoviesBinding.bindState(
        moviesUiState: StateFlow<MoviesUiState>,
        pagingData: Flow<PagingData<Movie>>,
        uiActions: (MoviesUiAction) -> Unit
    ) {
        val repoAdapter = MoviesAdapter()
        val header = MoviesLoadStateAdapter { repoAdapter.retry() }

        rvMovies.adapter = repoAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = MoviesLoadStateAdapter { repoAdapter.retry() }
        )
        bindSearch(
            moviesUiState = moviesUiState,
            onQueryChanged = uiActions
        )
        bindList(
            header = header,
            repoAdapter = repoAdapter,
            moviesUiState = moviesUiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun ActivityMoviesBinding.bindSearch(
        moviesUiState: StateFlow<MoviesUiState>,
        onQueryChanged: (MoviesUiAction.Search) -> Unit
    ) {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        etSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            moviesUiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(etSearch::setText)
        }
    }

    private fun ActivityMoviesBinding.updateRepoListFromInput(onQueryChanged: (MoviesUiAction.Search) -> Unit) {
        etSearch.text.trim().let {
            if (it.isNotEmpty()) {
                rvMovies.scrollToPosition(0)
                onQueryChanged(MoviesUiAction.Search(query = it.toString()))
            }
        }
    }

    private fun ActivityMoviesBinding.bindList(
        header: MoviesLoadStateAdapter,
        repoAdapter: MoviesAdapter,
        moviesUiState: StateFlow<MoviesUiState>,
        pagingData: Flow<PagingData<Movie>>,
        onScrollChanged: (MoviesUiAction.Scroll) -> Unit
    ) {
        btRetry.setOnClickListener { repoAdapter.retry() }
        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(MoviesUiAction.Scroll(currentQuery = moviesUiState.value.query))
            }
        })
        val notLoading = repoAdapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = moviesUiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(repoAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) rvMovies.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            repoAdapter.loadStateFlow.collect { loadState ->
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && repoAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty = loadState.refresh is LoadState.NotLoading && repoAdapter.itemCount == 0

                tvEmptyList.isVisible = isListEmpty

                rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading

                pbLoader.isVisible = loadState.mediator?.refresh is LoadState.Loading

                btRetry.isVisible = loadState.mediator?.refresh is LoadState.Error && repoAdapter.itemCount == 0

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@MoviesActivity,
                        "${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
