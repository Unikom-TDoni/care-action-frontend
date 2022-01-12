package edu.rpl.careaction.feature.task.data

import edu.rpl.careaction.core.utility.TaskUtility
import edu.rpl.careaction.feature.task.data.dao.TaskLocalDataSource
import edu.rpl.careaction.feature.task.data.dao.TaskRemoteDataSource
import edu.rpl.careaction.feature.task.domain.entity.HealthyLifestyleRoutines
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.domain.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskLocalDataSource: TaskLocalDataSource,
    private val taskRemoteDataSource: TaskRemoteDataSource
) : Repository() {

    override val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    fun fetchHealthyLifestyleRoutine() = flow {
        var response = taskLocalDataSource.loadHealthyLifestyleRoutineTask()
        if (response == null) {
            response = TaskUtility.generateDefaultHealthyLifestyleRoutineTask()
            updateHealthyLifestyleRoutine(response)
        }
        emit(ApiResult.Success<Collection<HealthyLifestyleRoutines>, ErrorResponse>(response))
    }.flowOn(defaultDispatcher)

    fun updateHealthyLifestyleRoutine(healthyLifestyleRoutines: Collection<HealthyLifestyleRoutines>) =
        taskLocalDataSource.saveHealthyLifestyleRoutineTask(healthyLifestyleRoutines)
}