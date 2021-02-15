package pt.amn.projectacademy.utils

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.widget.Toast
import pt.amn.projectacademy.R
import java.util.*


class CalendarHelper(val context: Context) {

    fun getCalendarId(): Long? {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )

        var calCursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + " = 1",
            null,
            CalendarContract.Calendars._ID + " ASC"
        )

         if (calCursor != null && calCursor.count <= 0) {
             calCursor = context.contentResolver.query(
                 CalendarContract.Calendars.CONTENT_URI,
                 projection,
                 CalendarContract.Calendars.VISIBLE + " = 1",
                 null,
                 CalendarContract.Calendars._ID + " ASC"
             )
         }

        if (calCursor != null && calCursor.moveToFirst()) {
            val calID = calCursor.getString(calCursor.getColumnIndex(projection[CALENDAR_ID_COLUMN]))
                .also {
                    calCursor.close()
                }

            return calID.toLong()
        }

        calCursor?.close()
        return null
    }

    private fun createEvent(
        context: Context,
        calId: Long,
        title: String = "",
        description: String = "",
        location: String = "",
        start: Long,
        end: Long = 0
    ) : Uri? {
        val event = ContentValues()
        event.run {
            put(CalendarContract.Events.CALENDAR_ID, calId)             // ID календаря мы получили ранее
            put(CalendarContract.Events.TITLE, title)                   // Название события
            put(CalendarContract.Events.DESCRIPTION, description)       // Описание события
            put(CalendarContract.Events.EVENT_LOCATION, location)       // Место проведения
            put(CalendarContract.Events.DTSTART, start)                 // время начала
            put(CalendarContract.Events.DTEND, end);                    // время окончания
            put(CalendarContract.Events.EVENT_TIMEZONE,
                Calendar.getInstance().timeZone.displayName)            // время окончания
        }

        val result = context.contentResolver.insert(
            getCalendarURI(),
            event
        )

        return result
    }

    private fun getCalendarURI() : Uri {
        return Uri.parse("content://com.android.calendar/events")
    }

    fun pickDate(description: String) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                val calendarHelper = CalendarHelper(context)
                val calendarId = calendarHelper.getCalendarId()

                if (calendarId != null) {
                    calendarHelper.createEvent(
                        context,
                        calendarId,
                        title = "Watch movie",
                        description = description,
                        start = calendar.timeInMillis,
                        end = calendar.timeInMillis
                    )
                }

                Toast.makeText(context, context.getString(R.string.event_add_to_calendar)
                    , Toast.LENGTH_SHORT).show()

            }, year, month, day
        )

        dateDialog.show()
    }

    companion object {
        private const val CALENDAR_ID_COLUMN = 0
    }

}