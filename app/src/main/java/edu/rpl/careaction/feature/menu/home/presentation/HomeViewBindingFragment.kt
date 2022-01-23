package edu.rpl.careaction.feature.menu.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import edu.rpl.careaction.databinding.FragmentMenuHomeBinding
import edu.rpl.careaction.feature.menu.MainMenuViewBindingFragmentDirections
import edu.rpl.careaction.feature.menu.home.domain.HomeData
import edu.rpl.careaction.feature.news.domain.dto.request.NewsRecommendedRequest
import edu.rpl.careaction.feature.news.presentation.adapter.NewsCategoryRecyclerViewAdapter
import edu.rpl.careaction.feature.news.presentation.adapter.NewsOverviewRecyclerViewAdapter
import edu.rpl.careaction.feature.support.bmi.BmiCalculator
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.ui.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewBindingFragment : ViewBindingFragment<FragmentMenuHomeBinding>() {

    private val homeViewModel: HomeViewModel by hiltNavGraphViewModels(R.id.bottom_toolbar_menu_nav_graph)

    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentMenuHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewAdapter = generateRecyclerViewAdapter()

        initButtonEvent()
        initRecyclerView(recyclerViewAdapter)
        initSharedFlowEvent(generateApiCallback(recyclerViewAdapter))
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fetchHomeData(NewsRecommendedRequest(5))
    }

    private fun initButtonEvent() {
        binding.btnTask.setOnClickListener {
            findNavController().navigate(R.id.healthyLifestyleRoutineViewBindingFragment)
        }
    }

    private fun generateRecyclerViewAdapter(): Pair<NewsCategoryRecyclerViewAdapter, NewsOverviewRecyclerViewAdapter> =
        Pair(NewsCategoryRecyclerViewAdapter(), NewsOverviewRecyclerViewAdapter {
            requireParentFragment().requireParentFragment().findNavController().navigate(
                MainMenuViewBindingFragmentDirections.actionMainMenuViewBindingFragmentToNewsDetailViewBindingFragment(
                    it
                )
            )
        })

    private fun initRecyclerView(recyclerViewAdapters: Pair<NewsCategoryRecyclerViewAdapter, NewsOverviewRecyclerViewAdapter>) {
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.viewNewsOverview.recyclerViewOverview.layoutManager = LinearLayoutManager(context)

        binding.recyclerViewCategory.adapter = recyclerViewAdapters.first
        binding.viewNewsOverview.recyclerViewOverview.adapter = recyclerViewAdapters.second
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<HomeData, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeSharedFlow.collect {
                    when (it) {
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Success -> apiCallback.successCallback(it)
                    }
                }
            }
        }

    private fun generateApiCallback(recyclerViewAdapters: Pair<NewsCategoryRecyclerViewAdapter, NewsOverviewRecyclerViewAdapter>): ApiCallback<HomeData, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()

                recyclerViewAdapters.first.submitList(it.response.newsCategories.toList())
                recyclerViewAdapters.second.submitList(it.response.newsOverviews.toList())
                ("\"" + it.response.motivation.content + "\"").also { text ->
                    binding.txtViewMotivation.text = text
                }

                val user = it.response.user
                binding.txtViewName.text = user.name

                if (user.weight != null && user.height != null) {
                    val bmiCalculator = BmiCalculator()
                    val bmi = bmiCalculator.calculateBmi(
                        user.weight.toDouble(),
                        bmiCalculator.convertCmToM(user.height.toDouble())
                    )
                    binding.txtViewBmiScore.text = String.format("%.2f", bmi)
                    binding.content.text = bmiCalculator.getBmiResult(bmi)
                    binding.progressCircular.progress = bmi.toInt()
                }
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