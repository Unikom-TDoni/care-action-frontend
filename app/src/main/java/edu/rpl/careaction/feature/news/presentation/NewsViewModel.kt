package edu.rpl.careaction.feature.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.feature.news.data.NewsRepository
import edu.rpl.careaction.feature.news.domain.dto.request.NewsDetailRequest
import edu.rpl.careaction.feature.news.domain.dto.request.NewsOverviewRequest
import edu.rpl.careaction.feature.news.domain.entity.NewsDetail
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _overviewSharedFlow =
        MutableSharedFlow<ApiResult<Collection<NewsOverview>, ErrorResponse>>()
    val overviewSharedFlow = _overviewSharedFlow.asSharedFlow()

    private val _detailSharedFlow =
        MutableSharedFlow<ApiResult<NewsDetail, ErrorResponse>>()
    val detailSharedFlow = _detailSharedFlow.asSharedFlow()

    fun fetchOverview(newsOverviewRequest: NewsOverviewRequest) =
        viewModelScope.launch {
            newsRepository.fetchOverview(newsOverviewRequest)
                .catch {
                    _overviewSharedFlow.emit(
                        ApiResult.Error(
                            ErrorResponse(
                                throwable = it
                            )
                        )
                    )
                }.collect {
                    _overviewSharedFlow.emit(it)
                }
        }

    fun fetchDetail(newsDetailRequest: NewsDetailRequest) =
        viewModelScope.launch {
            newsRepository.fetchDetail(newsDetailRequest)
                .catch {
                    _detailSharedFlow.emit(
                        ApiResult.Error(
                            ErrorResponse(
                                throwable = it
                            )
                        )
                    )
                }.collect {
                    _detailSharedFlow.emit(it)
                }
        }
}