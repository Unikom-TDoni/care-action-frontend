package edu.rpl.careaction.feature.user.data

import edu.rpl.careaction.feature.user.data.dao.UserLocalDataSource
import edu.rpl.careaction.feature.user.data.dao.UserRemoteDataSource
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.dto.request.ProfileRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.dto.response.toUser
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) {
    suspend fun login(loginRequest: LoginRequest): Flow<ApiResult<User, String>> =
        flow {
            emit(ApiResult.Loading())
            val response = userRemoteDataSource.login(loginRequest)
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        val userEntity = it.toUser()
                        userLocalDataSource.saveUser(userEntity)
                        emit(ApiResult.Success(userEntity))
                    } ?: emit(ApiResult.Error("Can't get body"))
                }
                else -> emit(ApiResult.Error("Can't get body"))
            }
        }

    suspend fun logout(): Flow<ApiResult<ResponseBody, String>> =
        flow {
            emit(ApiResult.Loading())
            val response = userRemoteDataSource.logout(userLocalDataSource.loadUser().token)
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        userLocalDataSource.logout()
                        emit(ApiResult.Success(it))
                    } ?: emit(ApiResult.Error("Can't get body"))
                }
                else -> emit(ApiResult.Error("Can't get body"))
            }
        }

    suspend fun register(registerRequest: RegisterRequest): Flow<ApiResult<User, String>> =
        flow {
            emit(ApiResult.Loading())
            val response = userRemoteDataSource.register(registerRequest)
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        val userEntity = it.toUser()
                        userLocalDataSource.saveUser(userEntity)
                        emit(ApiResult.Success(userEntity))
                    } ?: emit(ApiResult.Error("Can't get body"))
                }
                else -> emit(ApiResult.Error("Can't get body"))
            }
        }

    suspend fun update(profileRequest: ProfileRequest): Flow<ApiResult<ResponseBody, String>> =
        flow {
            emit(ApiResult.Loading())
            val response =
                userRemoteDataSource.update(profileRequest, userLocalDataSource.loadUser().token)
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(ApiResult.Success(it))
                    } ?: emit(ApiResult.Error("Can't get body"))
                }
                else -> emit(ApiResult.Error("Can't get body"))
            }
        }
}