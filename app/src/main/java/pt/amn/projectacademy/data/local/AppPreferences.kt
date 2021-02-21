package pt.amn.projectacademy.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferences @Inject constructor(val sharedPreferences: SharedPreferences) {

    fun saveCalendarRational(isRationaleCalendarShown: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN, isRationaleCalendarShown)
            .apply()
    }

    fun getCalendarRational(): Boolean {
        return sharedPreferences.getBoolean(
            KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN,
            false
        )
    }

    companion object {
        private const val KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN = "KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN_APP"
    }
}