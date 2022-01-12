package edu.rpl.careaction.feature.task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.task.data.TaskRepository
import edu.rpl.careaction.feature.task.domain.entity.HealthyLifestyleRoutines
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _healthyLifestyleRoutinesSharedFlow =
        MutableSharedFlow<ApiResult<Collection<HealthyLifestyleRoutines>, ErrorResponse>>()
    val healthyLifestyleRoutinesSharedFlow = _healthyLifestyleRoutinesSharedFlow.asSharedFlow()

    fun fetchHealthyLifestyleRoutine() =
        viewModelScope.launch {
            taskRepository.fetchHealthyLifestyleRoutine().collect {
                _healthyLifestyleRoutinesSharedFlow.emit(it)
            }
        }

    fun updateHealthyLifestyleRoutine(healthyLifestyleRoutines: Collection<HealthyLifestyleRoutines>) =
        taskRepository.updateHealthyLifestyleRoutine(healthyLifestyleRoutines)
}