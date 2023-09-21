package me.aleksandarzekovic.labsearchwithpaging3.ui.movies.items.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie
import me.aleksandarzekovic.labsearchwithpaging3.databinding.ItemMovieBinding

class MoviesAdapter : PagingDataAdapter<Movie, MovieViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie -> holder.bind(movie) }
    }

    companion object {
        private val Comparator = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem
        }
    }
}