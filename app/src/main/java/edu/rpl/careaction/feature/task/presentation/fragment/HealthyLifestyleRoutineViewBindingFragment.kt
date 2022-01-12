package edu.rpl.careaction.feature.task.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.ApplicationNavGraphDirections
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentMenuHealthyLifestyleRoutineBinding
import edu.rpl.careaction.feature.support.error.ErrorParcelable
import edu.rpl.careaction.feature.support.loading.LoadingDialogFragment
import edu.rpl.careaction.feature.task.domain.entity.HealthyLifestyleRoutines
import edu.rpl.careaction.feature.task.presentation.TaskViewModel
import edu.rpl.careaction.feature.task.presentation.adapter.HealthyLifestyleRoutineRecyclerViewAdapter
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.ui.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HealthyLifestyleRoutineViewBindingFragment :
    ViewBindingFragment<FragmentMenuHealthyLifestyleRoutineBinding>() {

    private val taskViewModel: TaskViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)
    private val healthyLifestyleRoutineRecyclerViewAdapter =
        HealthyLifestyleRoutineRecyclerViewAdapter()

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentMenuHealthyLifestyleRoutineBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerview()
        initSharedFlowEvent(generateApiCallback())
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.fetchHealthyLifestyleRoutine()
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<Collection<HealthyLifestyleRoutines>, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                taskViewModel.healthyLifestyleRoutinesSharedFlow.collect {
                    when (it) {
                        is ApiResult.Success -> apiCallback.successCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                    }
                }
            }
        }

    private fun initRecyclerview() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        healthyLifestyleRoutineRecyclerViewAdapter.onTaskIsChecked =
            { taskViewModel.updateHealthyLifestyleRoutine(it) }
        binding.recyclerView.adapter = healthyLifestyleRoutineRecyclerViewAdapter
    }

    private fun initToolbar() =
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    private fun generateApiCallback(): ApiCallback<Collection<HealthyLifestyleRoutines>, ErrorResponse> =
        ApiCallback(
            successCallback = fun(success) {
                LoadingDialogFragment.dismiss()
                healthyLifestyleRoutineRecyclerViewAdapter.submitList(success.response.toList())
            },
            loadingCallback = fun(_) {
                LoadingDialogFragment.show(
                    parentFragmentManager,
                    String()
                )
            },
            errorCallback = fun(error) {
                LoadingDialogFragment.dismiss()
                error.response.throwable?.let {
                    findNavController().navigate(
                        ApplicationNavGraphDirections.actionToErrorViewBindingFragment(
                            ErrorParcelable(it)
                        )
                    )
                } ?: Toast.makeText(context, error.response.message, Toast.LENGTH_SHORT).show()
            }
        )
}