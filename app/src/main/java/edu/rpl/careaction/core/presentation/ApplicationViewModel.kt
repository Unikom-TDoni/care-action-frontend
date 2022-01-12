package edu.rpl.careaction.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.user.data.UserRepository
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userSharedFlow = MutableSharedFlow<ApiResult<User, ErrorResponse>>()
    val userSharedFlow = _userSharedFlow.asSharedFlow()

    var isDataReady = false
        private set

    fun hideSplashScreenWhenActive() {
        isDataReady = true
    }

    fun fetchUser() {
        viewModelScope.launch {
            userRepository.fetch().collect {
                delay(3000)
                _userSharedFlow.emit(it)
            }
        }
    }
}