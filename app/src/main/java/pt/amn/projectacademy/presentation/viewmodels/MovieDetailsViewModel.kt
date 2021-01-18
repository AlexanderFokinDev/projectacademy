package pt.amn.projectacademy.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.amn.projectacademy.di.MainApplication
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.usecases.GetActorListUseCase

class MovieDetailsViewModel(movieId: Int) : ViewModel() {

    private val interactor = GetActorListUseCase(MainApplication.getRepository())

    // Недоступные вне класса изменяемые данные. Можно менять их только внутри этого класса
    private val _mutableActorsList: MutableLiveData<List<Actor>> by lazy {

        MutableLiveData<List<Actor>>().also { itList ->
            viewModelScope.launch {
                interactor.execute(movieId).also { result ->
                    when (result) {
                        is GetActorListUseCase.Result.Success ->
                            itList.value = result.data
                        is GetActorListUseCase.Result.Error ->
                            itList.value = emptyList()
                    }
                }
            }
        }
    }

    // Снаружи будет доступна переменная типа LiveData, на нее можно только подписываться, изменить
    // хранящиеся внутри данные нельзя
    val actorsList: LiveData<List<Actor>> get() = _mutableActorsList

}

private const val TAG = "MovieDetailsViewModel"