package pt.amn.projectacademy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), FragmentMoviesList.MoviesListFragmentClicks,
            FragmentMoviesDetails.MovieDetailsFragmentClicks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragments_container, FragmentMoviesList.newInstance())
                    .commit()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }

    override fun cardClick() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragments_container, FragmentMoviesDetails.newInstance())
                .addToBackStack(null)
                .commit()
    }

    override fun backClick() {
        onBackPressed()
    }

}