package me.aleksandarzekovic.labsearchwithpaging3.ui.movies.items.moviesload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import me.aleksandarzekovic.labsearchwithpaging3.databinding.ItemLoadStateBinding

class MoviesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MoviesLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: MoviesLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = MoviesLoadStateViewHolder(
        ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
}
