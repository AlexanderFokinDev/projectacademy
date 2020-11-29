package pt.amn.projectacademy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.amn.projectacademy.databinding.FragmentMoviesDetailsBinding

class FragmentMoviesDetails : Fragment() {

    private var _binding : FragmentMoviesDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var listener : MovieDetailsFragmentClicks? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPath.setOnClickListener {
            listener?.backClick()
        }
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

    companion object {
        /** @return A new instance of fragment FragmentMoviesDetails.*/
        @JvmStatic
        fun newInstance() = FragmentMoviesDetails()
    }

    interface MovieDetailsFragmentClicks {
        fun backClick()
    }

}