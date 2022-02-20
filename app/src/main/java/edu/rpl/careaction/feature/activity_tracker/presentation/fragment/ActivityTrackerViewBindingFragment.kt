package edu.rpl.careaction.feature.activity_tracker.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.databinding.FragmentMenuActivityTrackerBinding
import edu.rpl.careaction.feature.activity_tracker.domain.dto.request.ActivityTrackerRequest
import edu.rpl.careaction.feature.activity_tracker.domain.entity.ActivityTracker
import edu.rpl.careaction.feature.activity_tracker.presentation.ActivityTrackerViewModel
import edu.rpl.careaction.feature.activity_tracker.presentation.adapter.ActivityTrackerRecyclerViewAdapter
import edu.rpl.careaction.feature.activity_tracker.presentation.worker.ActivityTrackerWorkManager
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.presentation.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class ActivityTrackerViewBindingFragment :
    ViewBindingFragment<FragmentMenuActivityTrackerBinding>() {

    private val activityTrackerViewModel: ActivityTrackerViewModel by hiltNavGraphViewModels(R.id.bottom_toolbar_menu_nav_graph)
    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentMenuActivityTrackerBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityTrackerRecyclerViewAdapter =
            ActivityTrackerRecyclerViewAdapter { activityTrackerViewModel.updateLocal(it) }

        initNavigationEvent()
        initRecyclerview(activityTrackerRecyclerViewAdapter)
        initSharedFlowEvent(
            generateApiCallback(
                activityTrackerRecyclerViewAdapter,
                ActivityTrackerWorkManager()
            )
        )
    }

    override fun onResume() {
        super.onResume()
        activityTrackerViewModel.fetch(ActivityTrackerRequest(Calendar.getInstance().time))
    }

    private fun initNavigationEvent() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<Collection<ActivityTracker>, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityTrackerViewModel.activityTrackerSharedFlow.collect {
                    when (it) {
                        is ApiResult.Success -> apiCallback.successCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                    }
                }
            }
        }

    private fun initRecyclerview(activityTrackerRecyclerViewAdapter: ActivityTrackerRecyclerViewAdapter) {
        binding.recyclerViewOverview.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewOverview.adapter = activityTrackerRecyclerViewAdapter
    }

    private fun generateApiCallback(
        activityTrackerRecyclerViewAdapter: ActivityTrackerRecyclerViewAdapter,
        activityTrackerWorkManager: ActivityTrackerWorkManager
    ): ApiCallback<Collection<ActivityTracker>, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                activityTrackerWorkManager.start(requireContext())
                activityTrackerRecyclerViewAdapter.submitList(it.response.toList())
            },
            loadingCallback = {
                DefaultApiCallbackUtility.loadingCallback(parentFragmentManager)
            },
            errorCallback = {
                DefaultApiCallbackUtility.errorCallback(
                    requireContext(),
                    requireParentFragment().requireParentFragment().findNavController(),
                    it.response
                )
            }
        )
}