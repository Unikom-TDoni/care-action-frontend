package edu.rpl.careaction.feature.user.data

import android.util.Log
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.domain.Repository
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.feature.user.data.dao.UserRemoteDataSource
import edu.rpl.careaction.feature.user.domain.dto.request.*
import edu.rpl.careaction.feature.user.domain.dto.response.UserResponse
import edu.rpl.careaction.feature.user.domain.dto.response.UserUpdateResponse
import edu.rpl.careaction.feature.user.domain.dto.response.toUser
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.FlowApiBuilder
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : Repository() {

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

    fun registerProfile(request: RegisterProfileRequest) =
        FlowApiBuilder<ResponseBody, ErrorResponse, ResponseBody>()
            .setApiCall {
                userRemoteDataSource.registerProfile(
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
            .setManageApiErrorResponse {
                onApiResponseError(it)
            }
            .setCoroutineContext(defaultDispatcher).build()

    fun update(request: UpdateProfileRequest) =
        FlowApiBuilder<User, ErrorResponse, UserUpdateResponse>()
            .setApiCall {
                userRemoteDataSource.update(
                    request.picture,
                    request.name,
                    request.weight,
                    request.height,
                    request.gender,
                    request.birthdate,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse {
                val userEntity = it.toUser(userLocalDataSource.load()!!)
                userLocalDataSource.save(userEntity)
                userEntity
            }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun updatePassword(updatePasswordRequest: UpdatePasswordRequest) =
        FlowApiBuilder<ResponseBody, ErrorResponse, ResponseBody>()
            .setApiCall {
                userRemoteDataSource.updatePassword(
                    updatePasswordRequest,
                    userLocalDataSource.load()!!.token
                )
            }
            .setDefaultErrorResponseInstance(ErrorResponse())
            .setManageApiSuccessResponse { it }
            .setManageApiErrorResponse { onApiResponseError(it) }
            .setCoroutineContext(defaultDispatcher).build()

    fun fetch() = flow {
        val response = userLocalDataSource.load()
        if (response == null) emit(ApiResult.Error<User, ErrorResponse>(ErrorResponse()))
        else emit(ApiResult.Success(response))
    }.flowOn(defaultDispatcher)

}