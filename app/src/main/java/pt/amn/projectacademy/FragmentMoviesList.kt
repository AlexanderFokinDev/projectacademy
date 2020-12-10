package pt.amn.projectacademy

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.*
import pt.amn.projectacademy.adapters.MoviesAdapter
import pt.amn.projectacademy.adapters.OnRecyclerMovieClicked
import pt.amn.projectacademy.data.loadMovies
import pt.amn.projectacademy.databinding.FragmentMoviesListBinding
import pt.amn.projectacademy.models.Movie
import java.io.IOException

class FragmentMoviesList : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var fragmentListener : MoviesListFragmentClicks? = null
    private lateinit var adapter : MoviesAdapter
    private var coroutineSupervisorScope = createSuperScope()

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
        coroutineSupervisorScope.cancel()
        super.onDestroyView()
    }

    private fun createSuperScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun updateData() {
        coroutineSupervisorScope.launch(superExceptionHandler) {
            adapter.bindMovies(loadMovies(requireContext()))
            withContext(Dispatchers.Main) { adapter.notifyDataSetChanged() }
        }
    }

    private val superExceptionHandler = CoroutineExceptionHandler { canceledContext, exception ->
        Log.e(TAG, "SuperExceptionHandler [canceledContext:$canceledContext]")
        coroutineSupervisorScope.launch {
            logExceptionSuspend("superExceptionHandler", exception)
        }
    }

    private suspend fun logExceptionSuspend(who: String, throwable: Throwable) = withContext(Dispatchers.Main) {
        Log.e(TAG, "$who::Failed", throwable)
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

private const val TAG = "FragmentMoviesList"