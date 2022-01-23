package edu.rpl.careaction.feature.menu.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.feature.support.setting.data.SettingRepository
import edu.rpl.careaction.feature.support.setting.domain.Setting
import edu.rpl.careaction.feature.user.data.UserRepository
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
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {
    private val _userSharedFlow = MutableSharedFlow<ApiResult<User, ErrorResponse>>()
    val userSharedFlow = _userSharedFlow.asSharedFlow()

    private val _defaultUserSharedFlow =
        MutableSharedFlow<ApiResult<ResponseBody, ErrorResponse>>()
    val defaultUserUpdateSharedFlow = _defaultUserSharedFlow.asSharedFlow()

    private val _settingSharedFlow = MutableSharedFlow<ApiResult<Setting, ErrorResponse>>()
    val settingSharedFlow = _settingSharedFlow.asSharedFlow()

    fun fetchUser() =
        viewModelScope.launch {
            userRepository.fetch().collect {
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

    fun fetchSetting() =
        viewModelScope.launch {
            settingRepository.fetch().collect {
                _settingSharedFlow.emit(it)
            }
        }

    fun saveSetting(setting: Setting) =
        settingRepository.save(setting)
}