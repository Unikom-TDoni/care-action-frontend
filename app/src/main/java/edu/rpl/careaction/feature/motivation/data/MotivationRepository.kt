package edu.rpl.careaction.feature.motivation.data

import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.data.Repository
import edu.rpl.careaction.feature.motivation.data.dao.MotivationRemoteDataSource
import edu.rpl.careaction.feature.motivation.domain.dto.MotivationResponse
import edu.rpl.careaction.feature.motivation.domain.entity.Motivation
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.module.api.FlowApiBuilder
import javax.inject.Inject

class MotivationRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val motivationRemoteDataSource: MotivationRemoteDataSource,
) : Repository() {

    fun fetch() =
        FlowApiBuilder<Motivation, ErrorResponse, MotivationResponse>()
            .setApiCall { motivationRemoteDataSource.fetch(userLocalDataSource.load()!!.token) }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toMotivation()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()
}