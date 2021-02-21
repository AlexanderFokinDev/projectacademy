package pt.amn.projectacademy.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pt.amn.projectacademy.R
import pt.amn.projectacademy.presentation.adapters.ActorsAdapter
import pt.amn.projectacademy.databinding.FragmentMoviesDetailsBinding
import pt.amn.projectacademy.utils.loadImage
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.utils.BASE_URL_BACKDROP_IMAGE
import pt.amn.projectacademy.presentation.viewmodels.MovieDetailsViewModel
import pt.amn.projectacademy.presentation.viewmodels.MovieDetailsViewModel.MovieDetailsViewModelFactory
import pt.amn.projectacademy.presentation.viewmodels.utils.Status
import pt.amn.projectacademy.utils.CalendarHelper
import javax.inject.Inject

private const val PARAM_MOVIE = "fragment_movie"

@AndroidEntryPoint
class FragmentMoviesDetails : Fragment() {

    private var _binding : FragmentMoviesDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var listener : MovieDetailsFragmentClicks? = null
    private val adapter : ActorsAdapter = ActorsAdapter()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var isRationaleReadCalendarShown = false
    private var isRationaleWriteCalendarShown = false

    @Inject
    lateinit var movieDetailsViewModelFactory:  MovieDetailsViewModelFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModel.provideFactory(movieDetailsViewModelFactory,
            requireArguments().getInt(PARAM_MOVIE))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorePreferencesData()

        binding.rvActors.adapter = adapter

        binding.tvPath.setOnClickListener {
            listener?.backClick()
        }

        binding.ivSchedule.setOnClickListener {

            var readPermissionGranted = false
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED -> readPermissionGranted = true

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) ->
                    showReadCalendarPermissionExplanationDialog()

                isRationaleReadCalendarShown -> showReadCalendarPermissionDeniedDialog()

                else -> requestReadCalendarPermission()
            }

            if (readPermissionGranted) {
                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_CALENDAR
                    ) == PackageManager.PERMISSION_GRANTED -> onReadWriteCalendarPermissionGranted()

                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR) ->
                        showWriteCalendarPermissionExplanationDialog()

                    isRationaleWriteCalendarShown -> showWriteCalendarPermissionDeniedDialog()

                    else -> {
                        requestWriteCalendarPermission()
                    }
                }
            }

        }

        viewModel.movie.observe(viewLifecycleOwner, Observer { resMovie ->
            when(resMovie.status) {
                Status.SUCCESS -> {
                    if(resMovie.data != null) {
                        resMovie.data.run {
                            binding.tvName.text = title
                            binding.tvStoryline.text = overview
                            binding.tvTag.text = getTag()
                            binding.tvReview.text = getReview()
                            binding.tvAge.text = getMinimumAge()
                            binding.ratingBar.rating = getRating()
                            binding.ivBackground.loadImage(
                                binding.root,
                                BASE_URL_BACKDROP_IMAGE + backdrop
                            )
                        }
                    } else
                        throw IllegalArgumentException("Movie not found")
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), resMovie.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                }
            }
        })

        viewModel.actorsList.observe(viewLifecycleOwner, Observer { resActors ->
            when(resActors.status) {
                Status.SUCCESS -> {
                    // load the list of actors
                    updateData(resActors.data ?: emptyList())
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), resActors.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                }
            }
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailsFragmentClicks) {
            listener = context
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onReadWriteCalendarPermissionGranted()
            } else {
                onReadWriteCalendarPermissionNotGranted()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        requestPermissionLauncher.unregister()
    }

    override fun onDestroyView() {
        _binding = null
        savePreferencesData()
        super.onDestroyView()
    }

    private fun requestReadCalendarPermission() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)
        }
    }

    private fun requestWriteCalendarPermission() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
        }
    }

    private fun onReadWriteCalendarPermissionGranted() {
        context?.let {
            val calendar = CalendarHelper(requireContext())
            val calendarId = calendar.getCalendarId()

            if (calendarId != null) {
                calendar.pickDate(binding.tvName.text.toString())
            }
        }
    }

    private fun onReadWriteCalendarPermissionNotGranted() {
        context?.let {
            Toast.makeText(context, R.string.permission_read_write_calendar_not_granted_text,
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun showReadCalendarPermissionExplanationDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_read_calendar_dialog_explanation_text)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    isRationaleReadCalendarShown = true
                    requestReadCalendarPermission()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showWriteCalendarPermissionExplanationDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_calendar_dialog_explanation_text)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    isRationaleWriteCalendarShown = true
                    requestWriteCalendarPermission()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showReadCalendarPermissionDeniedDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_read_calendar_dialog_denied_text)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + it.packageName)
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showWriteCalendarPermissionDeniedDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_calendar_dialog_denied_text)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + it.packageName)
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun savePreferencesData() {
        activity?.let {
            sharedPreferences.edit()
                .putBoolean(KEY_READ_CALENDAR_PERMISSION_RATIONALE_SHOWN, isRationaleReadCalendarShown)
                .putBoolean(KEY_WRITE_CALENDAR_PERMISSION_RATIONALE_SHOWN, isRationaleWriteCalendarShown)
                .apply()
        }
    }

    private fun restorePreferencesData() {
        isRationaleReadCalendarShown = sharedPreferences.getBoolean(
            KEY_READ_CALENDAR_PERMISSION_RATIONALE_SHOWN,
            false
        ) ?: false

        isRationaleWriteCalendarShown = sharedPreferences.getBoolean(
            KEY_WRITE_CALENDAR_PERMISSION_RATIONALE_SHOWN,
            false
        ) ?: false
    }

    private fun updateData(actorsList: List<Actor>) {
        adapter.bindActors(actorsList)
        adapter.notifyDataSetChanged()
    }

    companion object {

        private const val KEY_READ_CALENDAR_PERMISSION_RATIONALE_SHOWN = "KEY_READ_CALENDAR_PERMISSION_RATIONALE_SHOWN_APP"
        private const val KEY_WRITE_CALENDAR_PERMISSION_RATIONALE_SHOWN = "KEY_WRITE_CALENDAR_PERMISSION_RATIONALE_SHOWN_APP"

        /** @return A new instance of fragment FragmentMoviesDetails.*/
        @JvmStatic
        fun newInstance(movieId : Int) = FragmentMoviesDetails().apply {
            arguments = Bundle().apply { putInt(PARAM_MOVIE, movieId) }
        }
    }

    interface MovieDetailsFragmentClicks {
        fun backClick()
    }

}