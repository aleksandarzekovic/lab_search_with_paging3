package me.aleksandarzekovic.labsearchwithpaging3.ui.movies

sealed class MoviesUiAction {
    data class Search(val query: String) : MoviesUiAction()
    data class Scroll(val currentQuery: String) : MoviesUiAction()
}