package edu.rpl.careaction.feature.news.data.dao

import edu.rpl.careaction.feature.news.domain.dto.request.OverviewNewsRequest
import edu.rpl.careaction.feature.news.domain.dto.response.NewsCategoryResponse
import edu.rpl.careaction.feature.news.domain.dto.response.NewsDetailResponse
import edu.rpl.careaction.feature.news.domain.dto.response.NewsOverviewResponse
import retrofit2.Response
import retrofit2.http.*

interface NewsRemoteDataSource {
    @GET("/api/news")
    suspend fun fetchOverview(
        @Body overviewNewsRequest: OverviewNewsRequest,
        @Header("Authorization") token: String
    ): Response<NewsOverviewResponse>

    @GET("/api/news/recommended")
    suspend fun fetchRecommendOverview(
        @Header("Authorization") token: String
    ): Response<NewsOverviewResponse>

    @GET("/api/news/{id}")
    suspend fun fetchDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<NewsDetailResponse>

    @GET("/api/category")
    suspend fun fetchCategory(
        @Header("Authorization") token: String
    ): Response<NewsCategoryResponse>
}