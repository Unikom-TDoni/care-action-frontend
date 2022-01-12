package edu.rpl.careaction.feature.task.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentMenuTaskBinding
import edu.rpl.careaction.feature.task.presentation.TaskViewModel
import edu.rpl.careaction.module.ui.ViewBindingFragment

class TaskViewBindingFragment : ViewBindingFragment<FragmentMenuTaskBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentMenuTaskBinding::inflate

    private val taskViewModel: TaskViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}