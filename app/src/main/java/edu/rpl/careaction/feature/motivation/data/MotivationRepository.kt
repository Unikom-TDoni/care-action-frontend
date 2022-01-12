package edu.rpl.careaction.feature.motivation.data

import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.motivation.data.dao.MotivationLocalDataSource
import edu.rpl.careaction.feature.motivation.data.dao.MotivationRemoteDataSource
import edu.rpl.careaction.feature.motivation.domain.dto.MotivationResponse
import edu.rpl.careaction.feature.motivation.domain.dto.toMotivation
import edu.rpl.careaction.feature.motivation.domain.entity.Motivation
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.api.FlowApiBuilder
import edu.rpl.careaction.module.domain.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import javax.inject.Inject

class MotivationRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val motivationLocalDataSource: MotivationLocalDataSource,
    private val motivationRemoteDataSource: MotivationRemoteDataSource,
) : Repository() {
    override val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    fun fetch() =
        FlowApiBuilder<Motivation, ErrorResponse, MotivationResponse>()
            .setApiCall { motivationRemoteDataSource.fetch(userLocalDataSource.load()!!.token) }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toMotivation()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    private fun onApiResponseError(responseBody: ResponseBody) =
        GsonMapperHelper.mapToDto(
            responseBody.charStream(),
            ErrorResponse::class.java
        )
}