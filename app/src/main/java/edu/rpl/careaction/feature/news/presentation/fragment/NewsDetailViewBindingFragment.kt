package edu.rpl.careaction.feature.news.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.databinding.FragmentNewsDetailBinding
import edu.rpl.careaction.feature.news.domain.dto.request.NewsDetailRequest
import edu.rpl.careaction.feature.news.domain.entity.NewsDetail
import edu.rpl.careaction.feature.news.presentation.NewsViewModel
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.ui.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewsDetailViewBindingFragment : ViewBindingFragment<FragmentNewsDetailBinding>() {

    private val newsViewModel: NewsViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)
    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentNewsDetailBinding::inflate

    private val newsDetailViewBindingFragmentArgs by navArgs<NewsDetailViewBindingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigationEvent()
        initShardFlowEvent(generateApiCallback())
    }

    override fun onResume() {
        super.onResume()
        newsViewModel.fetchDetail(NewsDetailRequest(newsDetailViewBindingFragmentArgs.id))
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

    private fun initShardFlowEvent(apiCallback: ApiCallback<NewsDetail, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.detailSharedFlow.collect {
                    when (it) {
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Success -> apiCallback.successCallback(it)
                    }
                }
            }
        }


    private fun generateApiCallback(): ApiCallback<NewsDetail, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()

                binding.tittle.text = it.response.title
                binding.content.text = it.response.content
                binding.txtViewName.text = it.response.author
                binding.toolbarTitle.text = it.response.category
                Glide.with(this).load(it.response.thumbnail).placeholder(R.drawable.img_placeholder)
                    .into(binding.image)
                binding.txtViewDate.text = DateUtility.convertDateToString(it.response.releaseDate)
            },
            loadingCallback = {
                DefaultApiCallbackUtility.loadingCallback(parentFragmentManager)
            },
            errorCallback = {
                DefaultApiCallbackUtility.errorCallback(
                    requireContext(),
                    findNavController(),
                    it.response
                )
            },
        )

}