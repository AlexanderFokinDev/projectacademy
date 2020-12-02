package pt.amn.projectacademy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pt.amn.projectacademy.adapters.ActorsAdapter
import pt.amn.projectacademy.databinding.FragmentMoviesDetailsBinding
import pt.amn.projectacademy.models.Movie

private const val PARAM_MOVIE = "fragment_movie"

class FragmentMoviesDetails : Fragment() {

    private var _binding : FragmentMoviesDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var fragmentMovie: Movie? = null

    private var listener : MovieDetailsFragmentClicks? = null

    private lateinit var adapter : ActorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentMovie = it.getParcelable(PARAM_MOVIE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = binding.rvActors
        adapter = ActorsAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext()
            , LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        binding.tvPath.setOnClickListener {
            listener?.backClick()
        }

        fragmentMovie?.let {
            binding.tvName.text = it.name
            binding.tvTag.text = it.tag
            binding.tvReview.text = it.getReview()
            binding.tvAge.text = it.getMinimumAge()
            binding.ratingBar.rating = it.getRatingFloat()
        }
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailsFragmentClicks) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun updateData() {
        adapter.bindActors(MoviesDataSource.getActors())
        adapter.notifyDataSetChanged()
    }

    companion object {

        /** @return A new instance of fragment FragmentMoviesDetails.*/
        @JvmStatic
        fun newInstance(movie : Movie) = FragmentMoviesDetails().apply {
            val bundle = Bundle()
            bundle.putParcelable(PARAM_MOVIE, movie)
            arguments = bundle
        }
    }

    interface MovieDetailsFragmentClicks {
        fun backClick()
    }

}