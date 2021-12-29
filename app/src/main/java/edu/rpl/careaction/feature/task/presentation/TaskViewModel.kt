package edu.rpl.careaction.feature.task.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.task.domain.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    fun a(){
        taskRepository.equals(1)
    }

}