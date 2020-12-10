package pt.amn.projectacademy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.amn.projectacademy.adapters.ActorsAdapter
import pt.amn.projectacademy.databinding.FragmentMoviesDetailsBinding
import pt.amn.projectacademy.models.Movie

private const val PARAM_MOVIE = "fragment_movie"

class FragmentMoviesDetails : Fragment() {

    private var _binding : FragmentMoviesDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var movie: Movie? = null
    private var listener : MovieDetailsFragmentClicks? = null
    private lateinit var adapter : ActorsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActorsAdapter()
        binding.rvActors.adapter = adapter

        binding.tvPath.setOnClickListener {
            listener?.backClick()
        }

        movie = requireArguments().getParcelable(PARAM_MOVIE)
        movie?.run {
            binding.tvName.text = title
            binding.tvStoryline.text = overview
            binding.tvTag.text = getTag()
            binding.tvReview.text = getReview()
            binding.tvAge.text = getMinimumAge()
            binding.ratingBar.rating = getRating()
            binding.ivBackground.loadImage(binding.root, backdrop)
        }

        // load the list of actors
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

    private fun updateData() {
        /*** movie - это nullable-переменная, поэтому вызываю таким образом или лучше было бы через
        movie!!, так как movie - всегда должно быть заполнено***/
        movie?.let { movieNotNull ->
            adapter.bindActors(movieNotNull.actors)
            adapter.notifyDataSetChanged()
        }
    }

    companion object {

        /** @return A new instance of fragment FragmentMoviesDetails.*/
        @JvmStatic
        fun newInstance(movie : Movie) = FragmentMoviesDetails().apply {
            arguments = Bundle().apply { putParcelable(PARAM_MOVIE, movie) }
        }
    }

    interface MovieDetailsFragmentClicks {
        fun backClick()
    }

}