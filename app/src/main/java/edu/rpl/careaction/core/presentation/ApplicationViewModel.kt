package edu.rpl.careaction.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.user.data.UserRepository
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.data.ApplicationRepository
import edu.rpl.careaction.core.domain.Setting
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val applicationRepository: ApplicationRepository
) : ViewModel() {

    private val _userSharedFlow = MutableSharedFlow<ApiResult<User, ErrorResponse>>()
    val userSharedFlow = _userSharedFlow.asSharedFlow()

    private val _settingSharedFlow = MutableSharedFlow<ApiResult<Setting, ErrorResponse>>()
    val settingSharedFlow = _settingSharedFlow.asSharedFlow()

    var isDataReady = false
        private set

    fun hideSplashScreenWhenActive() {
        isDataReady = true
    }

    fun fetchUser() =
        viewModelScope.launch {
            userRepository.fetch().catch {
                _userSharedFlow.emit(
                    ApiResult.Error(
                        ErrorResponse(
                            throwable = it
                        )
                    )
                )
            }.collect {
                delay(3000)
                _userSharedFlow.emit(it)
            }
        }

    fun fetchSetting() =
        viewModelScope.launch {
            applicationRepository.fetchSetting().catch {
                _settingSharedFlow.emit(
                    ApiResult.Error(
                        ErrorResponse(
                            throwable = it
                        )
                    )
                )
            }.collect {
                _settingSharedFlow.emit(it)
            }
        }
}