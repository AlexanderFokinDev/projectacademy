package pt.amn.projectacademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.amn.projectacademy.R
import pt.amn.projectacademy.databinding.ViewHolderMovieBinding
import pt.amn.projectacademy.models.Movie

class MoviesAdapter(private val listener: OnRecyclerMovieClicked)
    : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private var movies: List<Movie> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.onBind(movies[position], listener)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun bindMovies(newMovies : List<Movie>) {
        movies = newMovies
    }

    class MoviesViewHolder(private val binding: ViewHolderMovieBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(movie: Movie, listener: OnRecyclerMovieClicked) {
            binding.run {
                tvName.text = movie.name
                tvTag.text = movie.tag
                tvAge.text = movie.getMinimumAge()
                tvTime.text = movie.getDuration()
                tvReview.text = movie.getReview()
                ratingBar.rating = movie.getRatingFloat()
                ivMovie.setImageResource(movie.imagePath)

                if (movie.like) {
                    ivLike.setImageResource(R.drawable.heart_like_filled)
                } else {
                    ivLike.setImageResource(R.drawable.heart_like)
                }

                movieCard.setOnClickListener {
                    listener.onClick(movie)
                }
            }
        }

    }
}

interface  OnRecyclerMovieClicked {
    fun onClick(movie : Movie)
}