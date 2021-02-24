package pt.amn.projectacademy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.transition.MaterialElevationScale
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
                openMovie(id, null)
            } else {
                openMovieList()
            }
        }
    }

    override fun cardClick(movie: Movie, sharedElement: View) {
        openMovie(movie.id.toLong(), sharedElement)
    }

    override fun backClick() {
        onBackPressed()
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    openMovie(id, null)
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

    fun openMovieList() {
        val fragment = FragmentMoviesList.newInstance()

        fragment.exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.duration_movie_transition).toLong()
        }
        fragment.reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.duration_movie_transition).toLong()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, fragment)
            .commit()
    }

    fun openMovie(movieId: Long, sharedElement: View?) {

        val fragment = FragmentMoviesDetails.newInstance(movieId.toInt())

        // Show animation
        if (sharedElement != null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(sharedElement, getString(R.string.movie_transition_name))
                .replace(R.id.fragments_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            // deeplink without animation
            openMovieList()

            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragments_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

}