package edu.rpl.careaction.feature.activity_tracker.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.rpl.careaction.feature.activity_tracker.data.ActivityTrackerRepository
import edu.rpl.careaction.feature.activity_tracker.domain.dto.request.ActivityTrackerRequest
import kotlinx.coroutines.flow.collect
import java.util.*

@HiltWorker
class ActivityTrackerCoroutineWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val activityTrackerRepository: ActivityTrackerRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val date = activityTrackerRepository.fetchDateLocal()
        val activities = activityTrackerRepository.fetchLocal()
        if (date == 0L || activities == null) return Result.failure()
        activityTrackerRepository.update(activities.map {
            ActivityTrackerRequest(
                Date(date),
                it.isChecked,
                it.id
            )
        }).collect { }
        return Result.success()
    }
}