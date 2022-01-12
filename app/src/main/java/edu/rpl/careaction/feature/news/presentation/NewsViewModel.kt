package edu.rpl.careaction.feature.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.feature.news.data.NewsRepository
import edu.rpl.careaction.feature.news.domain.entity.NewsCategory
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _overviewSharedFlow = MutableSharedFlow<ApiResult<Collection<NewsCategory>, ErrorResponse>>()
    val overviewSharedFlow = _overviewSharedFlow.asSharedFlow()

    fun fetchOverview() =
        viewModelScope.launch {

        }
}