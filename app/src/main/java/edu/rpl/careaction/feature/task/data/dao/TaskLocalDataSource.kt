package edu.rpl.careaction.feature.task.data.dao

import com.google.gson.reflect.TypeToken
import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.core.data.ApplicationSharedPreferenceManager
import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.task.domain.entity.HealthyLifestyleRoutines
import javax.inject.Inject

class TaskLocalDataSource @Inject constructor(
    private val applicationSharedPreferenceManager: ApplicationSharedPreferenceManager
) {
    fun saveHealthyLifestyleRoutineTask(healthyLifestyleRoutines: Collection<HealthyLifestyleRoutines>) =
        applicationSharedPreferenceManager.store(
            mapOf(
                SharedPreferenceKey.HEALTHYLIFESTYLEROUTINE to GsonMapperHelper.mapToJson(
                    healthyLifestyleRoutines
                )
            )
        )

    fun loadHealthyLifestyleRoutineTask() =
        GsonMapperHelper.mapStringToDtoCollection<Collection<HealthyLifestyleRoutines>?>(
            applicationSharedPreferenceManager.getString(SharedPreferenceKey.HEALTHYLIFESTYLEROUTINE),
            object : TypeToken<Collection<HealthyLifestyleRoutines>>() {}.type
        )
}