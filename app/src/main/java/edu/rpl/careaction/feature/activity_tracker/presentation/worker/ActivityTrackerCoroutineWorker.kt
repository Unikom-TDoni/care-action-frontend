package edu.rpl.careaction.feature.activity_tracker.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.rpl.careaction.feature.activity_tracker.data.ActivityTrackerRepository
import edu.rpl.careaction.feature.activity_tracker.domain.dto.request.ActivityTrackerRequest
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import java.util.*

@HiltWorker
class ActivityTrackerCoroutineWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val activityTrackerRepository: ActivityTrackerRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        activityTrackerRepository.fetchLocal()
            .combine(activityTrackerRepository.fetchDateLocal()) { activityTrackers, date ->
                if (activityTrackers is ApiResult.Success && date is ApiResult.Success)
                    activityTrackerRepository.update(activityTrackers.response.map {
                        ActivityTrackerRequest(
                            Date(date.response),
                            it.isChecked,
                            it.id
                        )
                    }).collect { }
            }.collect { }
        return Result.success()
    }
}