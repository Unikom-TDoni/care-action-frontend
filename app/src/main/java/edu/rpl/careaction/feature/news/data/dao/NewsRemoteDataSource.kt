package edu.rpl.careaction.feature.news.data.dao

import edu.rpl.careaction.feature.news.domain.dto.request.NewsDetailRequest
import edu.rpl.careaction.feature.news.domain.dto.request.NewsOverviewRequest
import edu.rpl.careaction.feature.news.domain.dto.request.NewsRecommendedRequest
import edu.rpl.careaction.feature.news.domain.dto.response.NewsCategoryResponse
import edu.rpl.careaction.feature.news.domain.dto.response.NewsDetailResponse
import edu.rpl.careaction.feature.news.domain.dto.response.NewsOverviewResponse
import retrofit2.Response
import retrofit2.http.*

interface NewsRemoteDataSource {
    @POST("/api/news")
    suspend fun fetchOverview(
        @Body newsOverviewRequest: NewsOverviewRequest,
        @Header("Authorization") token: String
    ): Response<NewsOverviewResponse>

    @POST("/api/news/recommended")
    suspend fun fetchRecommendOverview(
        @Body newsRecommendedRequest: NewsRecommendedRequest,
        @Header("Authorization") token: String
    ): Response<NewsOverviewResponse>

    @POST("/api/news/detail")
    suspend fun fetchDetail(
        @Body newsDetailRequest: NewsDetailRequest,
        @Header("Authorization") token: String
    ): Response<NewsDetailResponse>

    @GET("/api/category")
    suspend fun fetchCategory(
        @Header("Authorization") token: String
    ): Response<NewsCategoryResponse>
}