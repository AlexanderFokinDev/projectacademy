package pt.amn.projectacademy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import pt.amn.projectacademy.adapters.MoviesAdapter
import pt.amn.projectacademy.adapters.OnRecyclerMovieClicked
import pt.amn.projectacademy.databinding.FragmentMoviesListBinding
import pt.amn.projectacademy.models.Movie

class FragmentMoviesList : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var fragmentListener : MoviesListFragmentClicks? = null

    private lateinit var adapter : MoviesAdapter

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

        val recycler = binding.rvMovies
        adapter = MoviesAdapter(recyclerListener)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2
            , GridLayoutManager.VERTICAL, false)
        recycler.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        updateData()
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

    fun updateData() {
        adapter.bindMovies(MoviesDataSource.getMovies())
        adapter.notifyDataSetChanged()
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