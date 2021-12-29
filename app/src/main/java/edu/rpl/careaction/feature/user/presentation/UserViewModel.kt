package edu.rpl.careaction.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.user.data.UserRepository
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.dto.request.ProfileRequest
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _userLoginSharedFlow = MutableSharedFlow<ApiResult<User, String>>()
    val userLoginSharedFlow = _userLoginSharedFlow.asSharedFlow()

    private val _userRegisterSharedFlow = MutableSharedFlow<ApiResult<User, String>>()
    val userRegisterSharedFlow = _userRegisterSharedFlow.asSharedFlow()

    private val _userUpdateSharedFlow = MutableSharedFlow<ApiResult<ResponseBody, String>>()
    val userUpdateSharedFlow = _userUpdateSharedFlow.asSharedFlow()

    private val _userLogoutSharedFlow = MutableSharedFlow<ApiResult<ResponseBody, String>>()
    val userLogoutSharedFlow = _userLogoutSharedFlow.asSharedFlow()

    fun login(loginRequest: LoginRequest) =
        viewModelScope.launch {
            userRepository.login(loginRequest).collect {
                _userLoginSharedFlow.emit(it)
            }
        }

    fun logout() =
        viewModelScope.launch {
            userRepository.logout().collect {
                _userLogoutSharedFlow.emit(it)
            }
        }

    fun register(registerRequest: RegisterRequest) =
        viewModelScope.launch {
            userRepository.register(registerRequest).collect {
                _userRegisterSharedFlow.emit(it)
            }
        }

    fun update(profileRequest: ProfileRequest) =
        viewModelScope.launch {
            userRepository.update(profileRequest).collect {
                _userUpdateSharedFlow.emit(it)
            }
        }
}