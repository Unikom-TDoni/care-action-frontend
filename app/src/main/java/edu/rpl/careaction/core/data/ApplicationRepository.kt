package edu.rpl.careaction.core.data

import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.data.dao.ApplicationLocalDataSource
import edu.rpl.careaction.core.domain.Setting
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApplicationRepository @Inject constructor(
    private val applicationLocalDataSource: ApplicationLocalDataSource
) : Repository() {

    fun saveSetting(setting: Setting) =
        applicationLocalDataSource.saveSetting(setting)

    fun fetchSetting() = flow {
        val response = applicationLocalDataSource.loadSetting()
        if (response == null) {
            val defaultSetting = Setting(true)
            applicationLocalDataSource.saveSetting(defaultSetting)
            emit(ApiResult.Success<Setting, ErrorResponse>(defaultSetting))
        } else emit(ApiResult.Success(response))
    }.flowOn(defaultDispatcher)
}