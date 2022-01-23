package edu.rpl.careaction.feature.activity_tracker.data

import edu.rpl.careaction.core.domain.Repository
import edu.rpl.careaction.feature.activity_tracker.data.dao.ActivityTrackerLocalDataSource
import edu.rpl.careaction.feature.activity_tracker.data.dao.ActivityTrackerRemoteDataSource
import edu.rpl.careaction.feature.activity_tracker.domain.dto.request.ActivityTrackerRequest
import edu.rpl.careaction.feature.activity_tracker.domain.dto.response.ActivityTrackerResponse
import edu.rpl.careaction.feature.activity_tracker.domain.dto.response.toActivityTrackers
import edu.rpl.careaction.feature.activity_tracker.domain.entity.ActivityTracker
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.module.api.FlowApiBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.util.*
import javax.inject.Inject

class ActivityTrackerRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val activityTrackerLocalDataSource: ActivityTrackerLocalDataSource,
    private val activityTrackerRemoteDataSource: ActivityTrackerRemoteDataSource
) : Repository() {

    fun fetch(activityTrackerRequest: ActivityTrackerRequest): Flow<ApiResult<Collection<ActivityTracker>, ErrorResponse>> {
        val response = fetchLocal()
        return if (response != null) flow { emit(ApiResult.Success(response)) }
        else FlowApiBuilder<Collection<ActivityTracker>, ErrorResponse, ActivityTrackerResponse>()
            .setApiCall {
                activityTrackerRemoteDataSource.fetch(
                    activityTrackerRequest,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                val result = it.toActivityTrackers()
                updateLocal(result)
                result
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()
    }

    fun update(activityTrackerRequest: Collection<ActivityTrackerRequest>) =
        FlowApiBuilder<ResponseBody, ErrorResponse, ResponseBody>()
            .setApiCall {
                for (i in 0..activityTrackerRequest.size - 2) {
                    activityTrackerRemoteDataSource.update(
                        activityTrackerRequest.elementAt(i),
                        userLocalDataSource.load()!!.token
                    )
                }
                activityTrackerRemoteDataSource.update(
                    activityTrackerRequest.last(),
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                activityTrackerLocalDataSource.delete()
                it
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetchLocal() =
        activityTrackerLocalDataSource.load()

    fun fetchDateLocal() =
        activityTrackerLocalDataSource.loadDate()

    fun updateLocal(activityTrackers: Collection<ActivityTracker>) {
        activityTrackerLocalDataSource.save(activityTrackers)
        activityTrackerLocalDataSource.saveDate(Calendar.getInstance().time)
    }

}