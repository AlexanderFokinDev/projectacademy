package pt.amn.projectacademy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagedList
import pt.amn.projectacademy.adapters.MoviesAdapter
import pt.amn.projectacademy.adapters.OnRecyclerMovieClicked
import pt.amn.projectacademy.databinding.FragmentMoviesListBinding
import pt.amn.projectacademy.models.Movie
import pt.amn.projectacademy.viewmodels.MoviesListViewModel

class FragmentMoviesList : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var fragmentListener : MoviesListFragmentClicks? = null
    private lateinit var adapter : MoviesAdapter

    private val viewModel: MoviesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MoviesAdapter(recyclerListener)
        binding.rvMovies.adapter = adapter

        viewModel.moviesList.observe(viewLifecycleOwner) { moviesList ->
            updateData(moviesList)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MoviesListFragmentClicks) {
            fragmentListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentListener = null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun updateData(moviesList: PagedList<Movie>) {
        adapter.submitList(moviesList)
    }

    private val recyclerListener = object : OnRecyclerMovieClicked {
        override fun onClick(movie: Movie) {
            fragmentListener?.cardClick(movie)
        }
    }

    companion object {
        /** @return A new instance of fragment FragmentMoviesList. */
        @JvmStatic
        fun newInstance() = FragmentMoviesList()
    }

    interface MoviesListFragmentClicks {
        fun cardClick(movie : Movie)
    }
}