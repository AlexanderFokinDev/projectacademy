package pt.amn.projectacademy.presentation.viewmodels

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.domain.models.Movie
import pt.amn.projectacademy.domain.repositories.MoviesRepository
import pt.amn.projectacademy.domain.usecases.GetActorListUseCase
import pt.amn.projectacademy.domain.usecases.GetMovieUseCase
import pt.amn.projectacademy.presentation.viewmodels.utils.Resource

class MovieDetailsViewModel @AssistedInject constructor(
    repository: MoviesRepository,
    @Assisted private val movieId: Int)
    : ViewModel() {

    private val interactorActors = GetActorListUseCase(repository)
    private val interactorMovie = GetMovieUseCase(repository)

    // Недоступные вне класса изменяемые данные. Можно менять их только внутри этого класса
    private val _mutableMovie: MutableLiveData<Resource<Movie>> by lazy {
        MutableLiveData<Resource<Movie>>().also {
            viewModelScope.launch {
                interactorMovie.execute(movieId).also { result ->
                    if(result.isError) {
                        _mutableMovie.postValue(
                            Resource.error(result.description, null))
                    } else {
                        _mutableMovie.postValue(
                            Resource.success(result.dataList.first()))
                    }
                }
            }
        }
    }

    private val _mutableActorsList: MutableLiveData<Resource<List<Actor>>> by lazy {

        MutableLiveData<Resource<List<Actor>>>().also {
            viewModelScope.launch {
                interactorActors.execute(movieId).also { result ->
                    if(result.isError) {
                        _mutableActorsList.postValue(
                            Resource.error(result.description, result.dataList))
                    } else {
                        _mutableActorsList.postValue(
                            Resource.success(result.dataList))
                    }
                }
            }
        }
    }

    // Снаружи будет доступна переменная типа LiveData, на нее можно только подписываться, изменить
    // хранящиеся внутри данные нельзя
    val movie: LiveData<Resource<Movie>> get() = _mutableMovie
    val actorsList: LiveData<Resource<List<Actor>>> get() = _mutableActorsList

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(movieId: Int): MovieDetailsViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: MovieDetailsViewModelFactory, movieId: Int)
        : ViewModelProvider.Factory = object  : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(movieId) as T
            }
        }
    }

}