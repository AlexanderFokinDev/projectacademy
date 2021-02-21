package pt.amn.projectacademy.domain.usecases

import pt.amn.projectacademy.domain.repositories.MoviesRepository

class PreferencesUseCase(private val repository: MoviesRepository) {

    fun saveCalendarRational(isRationaleCalendarShown: Boolean) {
        repository.saveCalendarRational(isRationaleCalendarShown)
    }

    fun getCalendarRational() : Boolean {
        return repository.getCalendarRational()
    }
}