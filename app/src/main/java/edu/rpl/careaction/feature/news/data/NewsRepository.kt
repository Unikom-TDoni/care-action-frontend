package edu.rpl.careaction.feature.news.data

import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.news.data.dao.NewsRemoteDataSource
import edu.rpl.careaction.feature.news.domain.dto.request.OverviewNewsRequest
import edu.rpl.careaction.feature.news.domain.dto.response.*
import edu.rpl.careaction.feature.news.domain.entity.NewsCategory
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.api.FlowApiBuilder
import edu.rpl.careaction.module.domain.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) : Repository() {
    override val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    fun fetchCategory() =
        FlowApiBuilder<Collection<NewsCategory>, ErrorResponse, NewsCategoryResponse>()
            .setApiCall {
                newsRemoteDataSource.fetchCategory(userLocalDataSource.load()!!.token)
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toNewsCategories()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetchOverview(overviewNewsRequest: OverviewNewsRequest) =
        FlowApiBuilder<Collection<NewsOverview>, ErrorResponse, NewsOverviewResponse>()
            .setApiCall {
                newsRemoteDataSource.fetchOverview(overviewNewsRequest, userLocalDataSource.load()!!.token)
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toNewsOverviews()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetchRecommendOverview() =
        FlowApiBuilder<Collection<NewsOverview>, ErrorResponse, NewsOverviewResponse>()
            .setApiCall {
                newsRemoteDataSource.fetchRecommendOverview(userLocalDataSource.load()!!.token)
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toNewsOverviews()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    private fun onApiResponseError(responseBody: ResponseBody) =
        GsonMapperHelper.mapToDto(
            responseBody.charStream(),
            ErrorResponse::class.java
        )
}