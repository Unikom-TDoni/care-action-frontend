package edu.rpl.careaction.feature.task.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import edu.rpl.careaction.databinding.FragmentLoginBinding
import edu.rpl.careaction.feature.task.presentation.TaskViewModel
import edu.rpl.careaction.module.ui.ViewBindingFragment

@AndroidEntryPoint
class TaskViewBindingFragment(
    override val bindingInflater: (LayoutInflater) -> ViewBinding
) : ViewBindingFragment<FragmentLoginBinding>() {

    private val taskViewModel: TaskViewModel by activityViewModels()

    private fun a(){
        taskViewModel.a()
    }

}