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

        val binding = ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false)

        return MoviesViewHolder(binding)
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
            binding.tvName.text = movie.name
            binding.tvTag.text = movie.tag
            binding.tvAge.text = movie.getMinimumAge()
            binding.tvTime.text = movie.getDuration()
            binding.tvReview.text = movie.getReview()
            binding.ratingBar.rating = movie.getRatingFloat()
            binding.ivMovie.setImageResource(movie.imagePath)

            if (movie.like) {
                binding.ivLike.setImageResource(R.drawable.heart_like_filled)
            } else {
                binding.ivLike.setImageResource(R.drawable.heart_like)
            }

            binding.movieCard.setOnClickListener {
                listener.onClick(movie)
            }
        }

    }
}

interface  OnRecyclerMovieClicked {
    fun onClick(movie : Movie)
}