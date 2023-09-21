package me.aleksandarzekovic.labsearchwithpaging3.ui.movies.items.movie

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import me.aleksandarzekovic.labsearchwithpaging3.R
import me.aleksandarzekovic.labsearchwithpaging3.data.Constants.BASE_IMAGE_URL
import me.aleksandarzekovic.labsearchwithpaging3.data.model.entity.Movie
import me.aleksandarzekovic.labsearchwithpaging3.databinding.ItemMovieBinding

class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) = with(binding) {
        tvTitle.text = movie.title.orEmpty()
        tvOverview.text = movie.overview.orEmpty()
        setMoviePoster(movie.posterPath)
        setRating(movie.voteAverage, movie.voteCount)
        tvRealiseDate.text = movie.releaseDate
    }

    private fun ItemMovieBinding.setMoviePoster(posterPath: String?) {
        val imageUrl = constructImageUrl(posterPath)
        Glide.with(itemView)
            .load(imageUrl)
            .transform(CenterCrop())
            .into(ivMoviePoster)
    }

    private fun constructImageUrl(posterPath: String?): String {
        return "$BASE_IMAGE_URL$posterPath"
    }

    private fun ItemMovieBinding.setRating(voteAverage: Float?, voteCount: Long?) {
        if (voteAverage != null && voteCount != null) {
            tvRating.text = root.resources.getString(R.string.vote, voteAverage, voteCount)
            tvRating.isVisible = true
        } else {
            tvRating.isVisible = false
        }
    }
}

