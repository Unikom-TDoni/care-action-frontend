package edu.rpl.careaction.feature.activity_tracker.data.dao

import edu.rpl.careaction.feature.activity_tracker.domain.dto.request.ActivityTrackerRequest
import retrofit2.Response
import edu.rpl.careaction.feature.activity_tracker.domain.dto.response.ActivityTrackerResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface ActivityTrackerRemoteDataSource {

    @POST("/api/activity")
    suspend fun fetch(
        @Body activityTrackerRequest: ActivityTrackerRequest,
        @Header("Authorization") token: String
    ): Response<ActivityTrackerResponse>

    @POST("/api/activity/checklist")
    suspend fun update(
        @Body activityTrackerRequest: ActivityTrackerRequest,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}