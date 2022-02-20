package edu.rpl.careaction.feature.news.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.databinding.FragmentNewsOverviewBinding
import edu.rpl.careaction.feature.news.domain.dto.request.NewsOverviewRequest
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview
import edu.rpl.careaction.feature.news.presentation.NewsViewModel
import edu.rpl.careaction.feature.news.presentation.adapter.NewsOverviewRecyclerViewAdapter
import edu.rpl.careaction.feature.page.main_menu.MainMenuViewBindingFragmentDirections
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.presentation.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView


class NewsOverviewViewBindingFragment : ViewBindingFragment<FragmentNewsOverviewBinding>() {

    private val newsViewModel: NewsViewModel by hiltNavGraphViewModels(R.id.bottom_toolbar_menu_nav_graph)
    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentNewsOverviewBinding::inflate

    private val newsOverviewViewBindingFragmentArgs by navArgs<NewsOverviewViewBindingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewAdapter = generateRecyclerViewAdapter()

        initSwipeLayout()
        initNavigationEvent()
        initSearchEvent(recyclerViewAdapter)
        initRecyclerView(recyclerViewAdapter)
        initSharedFlowEvent(generateApiCallback(recyclerViewAdapter))
    }

    override fun onResume() {
        super.onResume()

        fetch()
        initViewDefaultValue()
    }

    private fun initViewDefaultValue() {
        binding.searchBar.setQuery(String(), false)
        binding.searchBar.isIconified = true
        binding.tittle.text =
            newsOverviewViewBindingFragmentArgs.categoryName ?: getString(R.string.txt_view_news)
    }

    private fun initSwipeLayout() {
        binding.swipeLayout.setColorSchemeResources(R.color.green)
        binding.swipeLayout.setOnRefreshListener {
            fetch()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun initSearchEvent(newsOverviewRecyclerViewAdapter: NewsOverviewRecyclerViewAdapter) {
        binding.searchBar.maxWidth = Int.MAX_VALUE
        binding.searchBar.setOnSearchClickListener {
            binding.tittle.visibility = View.GONE
            binding.toolbar.navigationIcon = null
        }

        binding.searchBar.setOnCloseListener {
            binding.tittle.visibility = View.VISIBLE
            binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_navigate_before_24)
            false
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                newsOverviewRecyclerViewAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsOverviewRecyclerViewAdapter.filter.filter(newText)
                return false
            }
        })
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

    private fun initSharedFlowEvent(apiCallback: ApiCallback<Collection<NewsOverview>, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.overviewSharedFlow.collect {
                    when (it) {
                        is ApiResult.Success -> apiCallback.successCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                    }
                }
            }
        }

    private fun initRecyclerView(adapter: NewsOverviewRecyclerViewAdapter) {
        val layoutManager = LinearLayoutManager(context)
        binding.viewNewsOverview.recyclerViewOverview.adapter = adapter
        binding.viewNewsOverview.recyclerViewOverview.layoutManager = layoutManager
        binding.viewNewsOverview.recyclerViewOverview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.swipeLayout.isEnabled =
                    layoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
    }

    private fun fetch() =
        newsViewModel.fetchOverview(
            NewsOverviewRequest(
                if (newsOverviewViewBindingFragmentArgs.idCategory == -1) null
                else newsOverviewViewBindingFragmentArgs.idCategory
            )
        )

    private fun generateRecyclerViewAdapter(): NewsOverviewRecyclerViewAdapter =
        NewsOverviewRecyclerViewAdapter {
            requireParentFragment().requireParentFragment().findNavController().navigate(
                MainMenuViewBindingFragmentDirections.actionMainMenuViewBindingFragmentToNewsDetailViewBindingFragment(
                    it
                )
            )
        }

    private fun generateApiCallback(adapter: NewsOverviewRecyclerViewAdapter): ApiCallback<Collection<NewsOverview>, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                adapter.submitList(it.response.toList())
                adapter.setFilteredItems()
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