package edu.rpl.careaction.core.utility

import edu.rpl.careaction.R
import edu.rpl.careaction.feature.task.domain.entity.HealthyLifestyleRoutines

class TaskUtility {
    companion object {
        fun generateDefaultHealthyLifestyleRoutineTask(): Collection<HealthyLifestyleRoutines> =
            listOf(
                HealthyLifestyleRoutines(
                    0,
                    R.drawable.ic_custom_connection_error_24,
                    "Drinking Water 2L",
                    false
                ),
                HealthyLifestyleRoutines(
                    1,
                    R.drawable.ic_custom_connection_error_24,
                    "Eat Food 1600-3000 Calories",
                    false
                ),
                HealthyLifestyleRoutines(
                    2,
                    R.drawable.ic_custom_connection_error_24,
                    "Workout 30 Minute",
                    false
                ),
                HealthyLifestyleRoutines(
                    3,
                    R.drawable.ic_custom_connection_error_24,
                    "Meditate 30 Minute",
                    false
                ),
                HealthyLifestyleRoutines(
                    4,
                    R.drawable.ic_custom_connection_error_24,
                    "Sleep 8 Hour",
                    false
                )
            )
    }
}