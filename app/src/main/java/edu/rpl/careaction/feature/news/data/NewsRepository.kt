package edu.rpl.careaction.feature.news.data

import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.data.Repository
import edu.rpl.careaction.feature.news.data.dao.NewsRemoteDataSource
import edu.rpl.careaction.feature.news.domain.dto.request.NewsDetailRequest
import edu.rpl.careaction.feature.news.domain.dto.request.NewsOverviewRequest
import edu.rpl.careaction.feature.news.domain.dto.request.NewsRecommendedRequest
import edu.rpl.careaction.feature.news.domain.dto.response.*
import edu.rpl.careaction.feature.news.domain.entity.NewsCategory
import edu.rpl.careaction.feature.news.domain.entity.NewsDetail
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.module.api.FlowApiBuilder
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) : Repository() {

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

    fun fetchOverview(newsOverviewRequest: NewsOverviewRequest) =
        FlowApiBuilder<Collection<NewsOverview>, ErrorResponse, NewsOverviewResponse>()
            .setApiCall {
                newsRemoteDataSource.fetchOverview(
                    newsOverviewRequest,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toNewsOverviews()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetchRecommendOverview(newsRecommendedRequest: NewsRecommendedRequest) =
        FlowApiBuilder<Collection<NewsOverview>, ErrorResponse, NewsOverviewResponse>()
            .setApiCall {
                newsRemoteDataSource.fetchRecommendOverview(
                    newsRecommendedRequest,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toNewsOverviews()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetchDetail(newsDetailRequest: NewsDetailRequest) =
        FlowApiBuilder<NewsDetail, ErrorResponse, NewsDetailResponse>()
            .setApiCall {
                newsRemoteDataSource.fetchDetail(
                    newsDetailRequest,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                it.toNewsDetail()
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()
}