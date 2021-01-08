package pt.amn.projectacademy.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.amn.projectacademy.api.TMDBService
import pt.amn.projectacademy.data.MoviesRepository
import pt.amn.projectacademy.models.Actor
import pt.amn.projectacademy.usecases.MoviesInteractor
import java.io.IOException

class MovieDetailsViewModel(movieId: Int) : ViewModel() {

    private val interactor = MoviesInteractor(MoviesRepository(TMDBService.create()), viewModelScope)

    // Недоступные вне класса изменяемые данные. Можно менять их только внутри этого класса
    private val _mutableActorsList: MutableLiveData<List<Actor>> by lazy {
        MutableLiveData<List<Actor>>().also {itList ->
            viewModelScope.launch {
                try {
                    itList.value = interactor.getMovieActors(movieId)
                } catch (e: IOException) {
                    logExceptionSuspend("viewModelExceptionHandler", e)
                }
            }
        }
    }

    // Снаружи будет доступна переменная типа LiveData, на нее можно только подписываться, изменить
    // хранящиеся внутри данные нельзя
    val actorsList: LiveData<List<Actor>> get() = _mutableActorsList

    private suspend fun logExceptionSuspend(who: String, throwable: Throwable) = withContext(
        Dispatchers.Main) {
        Log.e(TAG, "$who::Failed", throwable)
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancel()
    }
}

private const val TAG = "MovieDetailsViewModel"