package pt.amn.projectacademy.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

    @Inject
    lateinit var movieDetailsViewModelFactory:  MovieDetailsViewModelFactory

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

        binding.run {

            rvActors.adapter = adapter

            tvPath.setOnClickListener {
                listener?.backClick()
            }

            ivSchedule.setOnClickListener {

                if (calendarPermissionsGranted()) {
                    onReadWriteCalendarPermissionGranted()
                } else {

                    Dexter.withContext(requireContext())
                        .withPermissions(
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR
                        )
                        .withListener(object : MultiplePermissionsListener {

                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                if (report?.areAllPermissionsGranted() == true) {
                                    onReadWriteCalendarPermissionGranted()
                                } else {
                                    if (report?.isAnyPermissionPermanentlyDenied == true
                                        && !viewModel.isRationaleCalendarShown
                                    ) {
                                        showCalendarPermissionDeniedDialog()
                                        viewModel.isRationaleCalendarShown = true
                                        savePreferencesData()
                                    }
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>?,
                                token: PermissionToken?
                            ) {
                                token?.continuePermissionRequest()
                            }

                        })
                        .check()
                }

            }

            viewModel.movie.observe(viewLifecycleOwner, Observer { resMovie ->
                when (resMovie.status) {
                    Status.SUCCESS -> {
                        if (resMovie.data != null) {
                            resMovie.data.run {
                                tvName.text = title
                                tvStoryline.text = overview
                                tvTag.text = getTag()
                                tvReview.text = getReview()
                                tvAge.text = getMinimumAge()
                                ratingBar.rating = getRating()
                                ivBackground.loadImage(
                                    root,
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
                when (resActors.status) {
                    Status.SUCCESS -> {
                        // load the list of actors
                        updateData(resActors.data ?: emptyList())
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), resActors.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {
                    }
                }
            })

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
        //requestPermissionLauncher.unregister()
    }

    override fun onDestroyView() {
        _binding = null
        savePreferencesData()
        super.onDestroyView()
    }

    private fun onReadWriteCalendarPermissionGranted() {
        val calendar = CalendarHelper(requireContext())
        val calendarId = calendar.getCalendarId()

        if (calendarId != null) {
            calendar.pickDate(binding.tvName.text.toString())
        }
    }

    private fun showCalendarPermissionDeniedDialog() {

        AlertDialog.Builder(requireContext())
            .setMessage(R.string.permission_read_calendar_dialog_denied_text)
            .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + requireContext().packageName)
                    )
                )
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun savePreferencesData() {
        viewModel.savePreferencesData()
    }

    private fun restorePreferencesData() {
        viewModel.restorePreferencesData()
    }

    private fun calendarPermissionsGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateData(actorsList: List<Actor>) {
        adapter.bindActors(actorsList)
        adapter.notifyDataSetChanged()
    }

    companion object {
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