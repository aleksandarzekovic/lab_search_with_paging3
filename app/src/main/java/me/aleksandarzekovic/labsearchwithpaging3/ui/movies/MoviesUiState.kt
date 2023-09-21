package me.aleksandarzekovic.labsearchwithpaging3.ui.movies

import me.aleksandarzekovic.labsearchwithpaging3.data.Constants.DEFAULT_QUERY


data class MoviesUiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)