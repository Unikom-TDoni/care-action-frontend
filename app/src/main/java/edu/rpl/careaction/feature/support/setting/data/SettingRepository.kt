package edu.rpl.careaction.feature.support.setting.data

import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.domain.Repository
import edu.rpl.careaction.feature.support.setting.data.dao.SettingLocalDataSource
import edu.rpl.careaction.feature.support.setting.domain.Setting
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val settingLocalDataSource: SettingLocalDataSource
) : Repository() {

    fun save(setting: Setting) =
        settingLocalDataSource.save(setting)

    fun fetch() = flow {
        val response = settingLocalDataSource.load()
        if (response == null) {
            val defaultSetting = Setting(true)
            settingLocalDataSource.save(defaultSetting)
            emit(ApiResult.Success<Setting, ErrorResponse>(defaultSetting))
        } else emit(ApiResult.Success(response))
    }.flowOn(defaultDispatcher)
}