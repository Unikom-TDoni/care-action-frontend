package edu.rpl.careaction.feature.support.menu.home.presentation

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
import edu.rpl.careaction.core.utility.BmiUtility
import edu.rpl.careaction.databinding.FragmentMenuHomeBinding
import edu.rpl.careaction.feature.news.presentation.adapter.NewsCategoryRecyclerViewAdapter
import edu.rpl.careaction.feature.news.presentation.adapter.NewsOverviewRecyclerViewAdapter
import edu.rpl.careaction.feature.support.error.ErrorParcelable
import edu.rpl.careaction.feature.support.loading.LoadingDialogFragment
import edu.rpl.careaction.feature.support.menu.home.domain.HomeData
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.ui.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewBindingFragment : ViewBindingFragment<FragmentMenuHomeBinding>() {

    private val homeViewModel: HomeViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)

    private val blogCategoryRecyclerViewAdapter = NewsCategoryRecyclerViewAdapter()
    private val blogOverviewRecyclerViewAdapter = NewsOverviewRecyclerViewAdapter()

    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentMenuHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonEvent()
        initRecyclerView()
        initSharedFlowEvent(generateApiCallback())
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fetchHomeData()
    }

    private fun initButtonEvent() {
        binding.btnTask.setOnClickListener {
            findNavController().navigate(R.id.healthyLifestyleRoutineViewBindingFragment)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.fragmentBlogOverview.recyclerViewBlog.layoutManager = LinearLayoutManager(context)

        binding.recyclerViewCategory.adapter = blogCategoryRecyclerViewAdapter
        binding.fragmentBlogOverview.recyclerViewBlog.adapter = blogOverviewRecyclerViewAdapter
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

    private fun generateApiCallback(): ApiCallback<HomeData, ErrorResponse> =
        ApiCallback(
            successCallback = fun(success) {
                LoadingDialogFragment.dismiss()
                blogCategoryRecyclerViewAdapter.submitList(success.response.newsCategories.toList())
                blogOverviewRecyclerViewAdapter.submitList(success.response.newsOverview.toList())
                binding.txtViewMotivation.text = "\"" + success.response.motivation.content + "\""

                val user = success.response.user
                binding.txtViewName.text = user.name
                if (user.weight != null && user.height != null) {
                    val bmi = BmiUtility.calculateBmi(
                        user.weight.toDouble(),
                        BmiUtility.convertCmToM(user.height.toDouble())
                    )
                    binding.txtViewBmi.text = String.format("%.2f", bmi)
                    binding.content.text = BmiUtility.getBmiResult(bmi)
                    binding.progressCircular.progress = bmi.toInt()
                }
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
                    requireParentFragment().requireParentFragment().findNavController().navigate(
                        ApplicationNavGraphDirections.actionToErrorViewBindingFragment(
                            ErrorParcelable(it)
                        )
                    )
                } ?: Toast.makeText(context, error.response.message, Toast.LENGTH_SHORT).show()
            }
        )
}