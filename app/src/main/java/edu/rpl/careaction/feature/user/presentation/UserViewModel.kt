package edu.rpl.careaction.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.user.data.UserRepository
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import kotlinx.coroutines.flow.*
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
    val defaultUserUpdateSharedFlow = _defaultUserSharedFlow.asSharedFlow()

    fun login(loginRequest: LoginRequest) =
        viewModelScope.launch {
            userRepository.login(loginRequest)
                .catch {
                    _userSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _userSharedFlow.emit(it)
                }
        }

    fun logout() =
        viewModelScope.launch {
            userRepository.logout()
                .catch {
                    _defaultUserSharedFlow.emit(ApiResult.Error(ErrorResponse(throwable = it)))
                }.collect {
                    _defaultUserSharedFlow.emit(it)
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

    fun update(registerProfileRequest: RegisterProfileRequest) =
        viewModelScope.launch {
            userRepository.update(registerProfileRequest)
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