package edu.rpl.careaction.feature.user.data.dao

import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.dto.request.ProfileRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.dto.response.LoginRegisterResponse
import retrofit2.http.*
import retrofit2.Response
import okhttp3.ResponseBody

interface UserRemoteDataSource {
    @POST("/api/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginRegisterResponse>

    @POST("/api/logout")
    suspend fun logout(
        @Header("Authorization") header: String
    ): Response<ResponseBody>

    @POST("/api/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<LoginRegisterResponse>

    @POST("/api/profile/change")
    suspend fun update(
        @Body profileRequest: ProfileRequest,
        @Header("Authorization") header: String
    ): Response<ResponseBody>
}