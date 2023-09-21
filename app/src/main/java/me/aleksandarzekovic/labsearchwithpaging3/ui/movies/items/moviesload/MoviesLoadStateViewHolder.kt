package me.aleksandarzekovic.labsearchwithpaging3.ui.movies.items.moviesload

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import me.aleksandarzekovic.labsearchwithpaging3.databinding.ItemLoadStateBinding

class MoviesLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) = with(binding) {
        if (loadState is LoadState.Error) {
            tvMessage.text = loadState.error.localizedMessage
        }
        pbLoader.isVisible = loadState is LoadState.Loading
        btRetry.isVisible = loadState is LoadState.Error
        tvMessage.isVisible = loadState is LoadState.Error
    }
}
