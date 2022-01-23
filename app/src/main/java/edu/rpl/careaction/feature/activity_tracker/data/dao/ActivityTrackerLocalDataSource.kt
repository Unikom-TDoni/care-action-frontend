package edu.rpl.careaction.feature.activity_tracker.data.dao

import javax.inject.Inject
import com.google.gson.reflect.TypeToken
import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.core.data.ApplicationSharedPreferenceManager
import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.activity_tracker.domain.entity.ActivityTracker
import java.util.*

class ActivityTrackerLocalDataSource @Inject constructor(
    private val applicationSharedPreferenceManager: ApplicationSharedPreferenceManager
) {
    fun save(activityTrackers: Collection<ActivityTracker>) =
        applicationSharedPreferenceManager.store(
            mapOf(
                SharedPreferenceKey.ACTIVITY_TRACKER to GsonMapperHelper.mapToJson(
                    activityTrackers
                )
            )
        )

    fun load() =
        GsonMapperHelper.mapStringToDtoCollection<Collection<ActivityTracker>?>(
            applicationSharedPreferenceManager.getString(SharedPreferenceKey.ACTIVITY_TRACKER),
            object : TypeToken<Collection<ActivityTracker>>() {}.type
        )

    fun saveDate(date: Date) =
        applicationSharedPreferenceManager.store(
            mapOf(
                SharedPreferenceKey.ACTIVITY_TRACKER_DATE to date.time
            )
        )

    fun loadDate() =
        applicationSharedPreferenceManager.getLong(SharedPreferenceKey.ACTIVITY_TRACKER_DATE)

    fun delete() =
        applicationSharedPreferenceManager.remove(setOf(SharedPreferenceKey.ACTIVITY_TRACKER))
}