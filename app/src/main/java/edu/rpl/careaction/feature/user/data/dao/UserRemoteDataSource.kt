package edu.rpl.careaction.feature.user.data.dao

import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.dto.request.UpdatePasswordRequest
import edu.rpl.careaction.feature.user.domain.dto.response.UserResponse
import edu.rpl.careaction.feature.user.domain.dto.response.UserUpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UserRemoteDataSource {
    @POST("/api/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<UserResponse>

    @POST("/api/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ResponseBody>

    @POST("/api/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<UserResponse>

    @POST("/api/profile/register")
    suspend fun registerProfile(
        @Body registerProfileRequest: RegisterProfileRequest,
        @Header("Authorization") token: String
    ): Response<ResponseBody>

    @Multipart
    @POST("/api/profile/change")
    suspend fun update(
        @Part picture: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("height") height: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Header("Authorization") token: String
    ): Response<UserUpdateResponse>

    @POST("/api/profile/password")
    suspend fun updatePassword(
        @Body updatePasswordRequest: UpdatePasswordRequest,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}