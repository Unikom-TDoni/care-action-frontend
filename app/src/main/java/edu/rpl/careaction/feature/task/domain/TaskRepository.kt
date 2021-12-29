package edu.rpl.careaction.feature.task.domain

import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskApi: TaskApi) {

    suspend fun getTask(){
        taskApi.equals(1)
    }
}