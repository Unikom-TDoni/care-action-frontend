package edu.rpl.careaction.core.utility

import android.app.DatePickerDialog
import android.content.Context
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateUtility {
    companion object {
        const val DATE_PATTERN_SERVER = "yyyy-MM-dd'T'HH:mm:ss"
        private const val DATE_PATTERN_DEFAULT = "dd MMMM yyyy"

        fun generateAgeFromDate(date: Date?): Int? =
            date?.let {
                val calendarBirthDate = Calendar.getInstance()
                val calendarCurrentDate = Calendar.getInstance()
                calendarBirthDate.time = it
                var age =
                    calendarCurrentDate.get(Calendar.YEAR) - calendarBirthDate.get(Calendar.YEAR)
                if (calendarCurrentDate.get(Calendar.DAY_OF_YEAR) < calendarBirthDate.get(Calendar.DAY_OF_YEAR))
                    age--
                return age
            }

        fun convertStringToDate(string: String): Date? =
            SimpleDateFormat(DATE_PATTERN_DEFAULT, Locale.getDefault()).parse(string)

        fun convertDateToString(date: Date): String =
            SimpleDateFormat(DATE_PATTERN_DEFAULT, Locale.getDefault()).format(date)

        fun convertStringToTimestamp(string: String): Timestamp =
            Timestamp.valueOf(string)

        fun generateDatePickerDialog(
            context: Context,
            onDatePicked: (pickedDate: Date) -> Unit
        ): DatePickerDialog {
            val calendar = Calendar.getInstance()
            val dialogResult = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    onDatePicked(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialogResult.datePicker.maxDate = Date().time
            return dialogResult
        }
    }
}