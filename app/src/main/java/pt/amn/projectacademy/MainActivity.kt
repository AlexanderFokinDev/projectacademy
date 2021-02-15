package pt.amn.projectacademy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.presentation.FragmentMoviesDetails
import pt.amn.projectacademy.presentation.FragmentMoviesList

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentMoviesList.MoviesListFragmentClicks,
            FragmentMoviesDetails.MovieDetailsFragmentClicks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val id = intent.data?.lastPathSegment?.toLongOrNull()
            if (id != null) {
                openMovie(id)
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, FragmentMoviesList.newInstance())
                    .commit()
            }
        }
    }

    override fun cardClick(movie : Movie) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragments_container, FragmentMoviesDetails.newInstance(movie.id))
                .addToBackStack(null)
                .commit()
    }

    override fun backClick() {
        onBackPressed()
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    openMovie(id)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    fun openMovie(movieId: Long) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, FragmentMoviesList.newInstance())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, FragmentMoviesDetails.newInstance(movieId.toInt()))
            .addToBackStack(null)
            .commit()
    }

}