package edu.rpl.careaction.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.feature.user.data.UserRepository
import edu.rpl.careaction.feature.user.domain.dto.request.*
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _userSharedFlow = MutableSharedFlow<ApiResult<User, ErrorResponse>>()
    val userSharedFlow = _userSharedFlow.asSharedFlow()

    private val _defaultUserSharedFlow =
        MutableSharedFlow<ApiResult<ResponseBody, ErrorResponse>>()
    val defaultUserShardFlow = _defaultUserSharedFlow.asSharedFlow()

    fun login(loginRequest: LoginRequest) =
        viewModelScope.launch {
            userRepository.login(loginRequest)
                .catch {
                    _userSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _userSharedFlow.emit(it)
                }
        }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            userRepository.register(registerRequest)
                .catch {
                    _userSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _userSharedFlow.emit(it)
                }
        }
    }

    fun registerProfile(registerProfileRequest: RegisterProfileRequest) =
        viewModelScope.launch {
            userRepository.registerProfile(registerProfileRequest)
                .catch {
                    _defaultUserSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _defaultUserSharedFlow.emit(it)
                }
        }

    fun update(updateProfileRequest: UpdateProfileRequest) =
        viewModelScope.launch {
            userRepository.update(updateProfileRequest)
                .catch {
                    _userSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _userSharedFlow.emit(it)
                }
        }

    fun updatePassword(updatePasswordRequest: UpdatePasswordRequest) =
        viewModelScope.launch {
            userRepository.updatePassword(updatePasswordRequest)
                .catch {
                    _defaultUserSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _defaultUserSharedFlow.emit(it)
                }

        }

    fun fetch() =
        viewModelScope.launch {
            userRepository.fetch().collect {
                _userSharedFlow.emit(it)
            }
        }
}