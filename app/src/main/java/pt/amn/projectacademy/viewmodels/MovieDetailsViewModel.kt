package pt.amn.projectacademy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.amn.projectacademy.models.Actor

class MovieDetailsViewModel(extActorList: List<Actor>) : ViewModel() {

    // Недоступные вне класса изменяемые данные. Можно менять их только внутри этого класса
    private val _mutableActorsList: MutableLiveData<List<Actor>> by lazy {
        MutableLiveData<List<Actor>>().also { itList ->
            itList.value = extActorList }
    }

    // Снаружи будет доступна переменная типа LiveData, на нее можно только подписываться, изменить
    // хранящиеся внутри данные нельзя
    val actorsList: LiveData<List<Actor>> get() = _mutableActorsList
}