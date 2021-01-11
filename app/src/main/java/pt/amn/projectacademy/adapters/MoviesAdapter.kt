package pt.amn.projectacademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pt.amn.projectacademy.databinding.ViewHolderMovieBinding
import pt.amn.projectacademy.models.Movie
import pt.amn.projectacademy.loadImage
import pt.amn.projectacademy.utilities.BASE_URL_POSTER_IMAGE

class MoviesAdapter(private val listener: OnRecyclerMovieClicked)
    : PagedListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.onBind(getItem(position), listener)
    }

    class MoviesViewHolder(private val binding: ViewHolderMovieBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(movie: Movie?, listener: OnRecyclerMovieClicked) {
            binding.run {
                tvName.text = movie?.title
                ratingBar.rating = movie?.getRating() ?: 0F
                tvAge.text = movie?.getMinimumAge()
                tvReleaseDate.text = movie?.releaseDate
                tvReview.text = movie?.getReview()
                tvTag.text = movie?.getTag()
                ivMovie.loadImage(binding.root, BASE_URL_POSTER_IMAGE + movie?.poster)

                movieCard.setOnClickListener {
                    if (movie != null) {
                        listener.onClick(movie)
                    }
                }
            }
        }

    }

    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface  OnRecyclerMovieClicked {
    fun onClick(movie : Movie)
}