package edu.rpl.careaction.core.utility

import edu.rpl.careaction.core.config.ApplicationConfig
import java.text.SimpleDateFormat
import java.util.*

class DateUtility {
    companion object {
        fun generateAgeFromDate(date: Date?): Int? =
            date?.let {
                val calendarBirthDate = Calendar.getInstance()
                val calendarCurrentDate = Calendar.getInstance()
                calendarBirthDate.time = it
                var age = calendarCurrentDate.get(Calendar.YEAR) - calendarBirthDate.get(Calendar.YEAR)
                if (calendarCurrentDate.get(Calendar.DAY_OF_YEAR) < calendarBirthDate.get(Calendar.DAY_OF_YEAR))
                    age--
                return age
            }

        fun convertStringToDate(string: String): Date? =
            SimpleDateFormat(ApplicationConfig.DATE_PATTERN, Locale.getDefault()).parse(string)

        fun convertDateToString(date: Date): String =
            SimpleDateFormat(ApplicationConfig.DATE_PATTERN, Locale.getDefault()).format(date)
    }
}