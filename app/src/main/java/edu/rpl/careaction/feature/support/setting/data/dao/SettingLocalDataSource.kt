package edu.rpl.careaction.feature.support.setting.data.dao

import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.core.data.ApplicationSharedPreferenceManager
import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.support.setting.domain.Setting
import javax.inject.Inject

class SettingLocalDataSource @Inject constructor(
    private val applicationSharedPreferenceManager: ApplicationSharedPreferenceManager
) {
    fun save(setting: Setting) =
        applicationSharedPreferenceManager.store(
            mapOf(
                SharedPreferenceKey.SETTING to GsonMapperHelper.mapToJson(
                    setting
                )
            )
        )

    fun load() =
        GsonMapperHelper.mapStringToDto(
            applicationSharedPreferenceManager.getString(SharedPreferenceKey.SETTING),
            Setting::class.java
        )
}