package pt.amn.projectacademy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.amn.projectacademy.models.Actor

class MovieDetailsViewModelFactory(private val extActorList: List<Actor>?) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(extActorList ?: emptyList())
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}