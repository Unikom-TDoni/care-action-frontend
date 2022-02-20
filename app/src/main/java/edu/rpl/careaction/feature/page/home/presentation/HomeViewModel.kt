package edu.rpl.careaction.feature.page.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.feature.page.home.domain.HomeData
import edu.rpl.careaction.feature.motivation.data.MotivationRepository
import edu.rpl.careaction.feature.news.data.NewsRepository
import edu.rpl.careaction.feature.news.domain.dto.request.NewsRecommendedRequest
import edu.rpl.careaction.feature.user.data.UserRepository
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val newsRepository: NewsRepository,
    private val motivationRepository: MotivationRepository
) : ViewModel() {

    private val _homeSharedFlow = MutableSharedFlow<ApiResult<HomeData, ErrorResponse>>()
    val homeSharedFlow = _homeSharedFlow.asSharedFlow()

    fun fetch(newsRecommendedRequest: NewsRecommendedRequest) {
        viewModelScope.launch {
            combine(
                userRepository.fetch(),
                motivationRepository.fetch(),
                newsRepository.fetchCategory(),
                newsRepository.fetchRecommendOverview(newsRecommendedRequest),
            ) { user, motivation, categories, recommendOverview ->
                val result = listOf(user, categories, recommendOverview, motivation)
                when {
                    result.any { it is ApiResult.Loading } -> ApiResult.Loading<HomeData, ErrorResponse>()
                    result.all { it is ApiResult.Success } -> {
                        val userResponse = user as ApiResult.Success
                        val categoryResponse = categories as ApiResult.Success
                        val motivationResponse = motivation as ApiResult.Success
                        val recommendOverviewResponse = recommendOverview as ApiResult.Success
                        ApiResult.Success(
                            HomeData(
                                userResponse.response,
                                motivationResponse.response,
                                categoryResponse.response,
                                recommendOverviewResponse.response
                            )
                        )
                    }
                    else -> ApiResult.Error(ErrorResponse())
                }
            }.catch {
                _homeSharedFlow.emit(
                    ApiResult.Error(
                        ErrorResponse(
                            throwable = it
                        )
                    )
                )
            }.collect {
                _homeSharedFlow.emit(it)
            }
        }
    }
}