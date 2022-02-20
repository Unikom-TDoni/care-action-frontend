package edu.rpl.careaction.feature.motivation.data.dao

import retrofit2.Response
import retrofit2.http.GET
import edu.rpl.careaction.feature.motivation.domain.dto.MotivationResponse
import retrofit2.http.Header

interface MotivationRemoteDataSource {
    @GET("/api/quotes")
    suspend fun fetch(
        @Header("Authorization") token: String
    ): Response<MotivationResponse>
}