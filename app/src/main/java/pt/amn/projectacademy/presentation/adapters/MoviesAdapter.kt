package pt.amn.projectacademy.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pt.amn.projectacademy.databinding.ViewHolderMovieBinding
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.utils.BASE_URL_POSTER_IMAGE

class MoviesAdapter(private val listener: OnRecyclerMovieClicked)
    : PagedListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {

        val movie = getItem(position);
        if (movie != null) {
            holder.onBind(movie, listener)
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            // т. е. в этот момент данные еще недоступны, можно сделать какую-то карточку заглушку
            // с фразой  Waiting... например
        }

    }

    class MoviesViewHolder(private val binding: ViewHolderMovieBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(movie: Movie, listener: OnRecyclerMovieClicked) {
            binding.run {
                tvName.text = movie.title
                ratingBar.rating = movie.getRating()
                tvAge.text = movie.getMinimumAge()
                tvReleaseDate.text = movie.releaseDate
                tvReview.text = movie.getReview()
                tvTag.text = movie.getTag()
                ivMovie.loadImage(binding.root, BASE_URL_POSTER_IMAGE + movie.poster)

                movieCard.setOnClickListener {
                    listener.onClick(movie)
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