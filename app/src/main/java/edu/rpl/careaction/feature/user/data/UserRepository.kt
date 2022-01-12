package edu.rpl.careaction.feature.user.data

import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.feature.user.data.dao.UserRemoteDataSource
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.dto.response.UserResponse
import edu.rpl.careaction.feature.user.domain.dto.response.toUser
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.api.FlowApiBuilder
import edu.rpl.careaction.module.domain.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : Repository() {
    override val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    fun login(request: LoginRequest) =
        FlowApiBuilder<User, ErrorResponse, UserResponse>()
            .setApiCall { userRemoteDataSource.login(request) }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                val userEntity = it.toUser()
                userLocalDataSource.save(userEntity)
                userEntity
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun logout() =
        FlowApiBuilder<ResponseBody, ErrorResponse, ResponseBody>()
            .setApiCall { userRemoteDataSource.logout(userLocalDataSource.load()!!.token) }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                userLocalDataSource.logout()
                it
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun register(request: RegisterRequest) =
        FlowApiBuilder<User, ErrorResponse, UserResponse>()
            .setApiCall { userRemoteDataSource.register(request) }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                val userEntity = it.toUser()
                userLocalDataSource.save(userEntity)
                userEntity
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun update(request: RegisterProfileRequest) =
        FlowApiBuilder<ResponseBody, ErrorResponse, ResponseBody>()
            .setApiCall {
                userRemoteDataSource.update(
                    request,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                val userEntity = userLocalDataSource.load()!!.copy(
                    weight = request.weight,
                    height = request.height,
                    gender = request.gender,
                    birthDate = request.birthdate,
                    age = DateUtility.generateAgeFromDate(request.birthdate)
                )
                userLocalDataSource.save(userEntity)
                it
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetch() = flow {
        val response = userLocalDataSource.load()
        if (response == null) emit(ApiResult.Error<User, ErrorResponse>(ErrorResponse()))
        else emit(ApiResult.Success(response))
    }.flowOn(defaultDispatcher)

    private fun onApiResponseError(responseBody: ResponseBody) =
        GsonMapperHelper.mapToDto(
            responseBody.charStream(),
            ErrorResponse::class.java
        )
}