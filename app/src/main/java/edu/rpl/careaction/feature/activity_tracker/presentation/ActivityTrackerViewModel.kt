package edu.rpl.careaction.feature.activity_tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.activity_tracker.data.ActivityTrackerRepository
import edu.rpl.careaction.feature.activity_tracker.domain.entity.ActivityTracker
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.feature.activity_tracker.domain.dto.request.ActivityTrackerRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityTrackerViewModel @Inject constructor(
    private val activityTrackerRepository: ActivityTrackerRepository
) : ViewModel() {

    private val _activityTrackerSharedFlow =
        MutableSharedFlow<ApiResult<Collection<ActivityTracker>, ErrorResponse>>()
    val activityTrackerSharedFlow = _activityTrackerSharedFlow.asSharedFlow()

    fun fetch(activityTrackerRequest: ActivityTrackerRequest) =
        viewModelScope.launch {
            activityTrackerRepository.fetchLocal()
                .catch {
                    _activityTrackerSharedFlow.emit(
                        ApiResult.Error(
                            ErrorResponse(
                                throwable = it
                            )
                        )
                    )
                }
                .collect {
                    when (it) {
                        is ApiResult.Success ->
                            _activityTrackerSharedFlow.emit(it)
                        else -> activityTrackerRepository.fetch(activityTrackerRequest)
                            .catch { error ->
                                _activityTrackerSharedFlow.emit(
                                    ApiResult.Error(
                                        ErrorResponse(
                                            throwable = error
                                        )
                                    )
                                )
                            }.collect { remoteResult ->
                                _activityTrackerSharedFlow.emit(remoteResult)
                            }
                    }
                }
        }

    fun updateLocal(healthyLifestyleRoutines: Collection<ActivityTracker>) =
        activityTrackerRepository.updateLocal(healthyLifestyleRoutines)
}