package edu.rpl.careaction.core.data.dao

import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.core.data.ApplicationSharedPreferenceManager
import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.core.domain.Setting
import javax.inject.Inject

class ApplicationLocalDataSource @Inject constructor(
    private val applicationSharedPreferenceManager: ApplicationSharedPreferenceManager
) {
    fun saveSetting(setting: Setting) =
        applicationSharedPreferenceManager.store(
            mapOf(
                SharedPreferenceKey.SETTING to GsonMapperHelper.mapToJson(
                    setting
                )
            )
        )

    fun loadSetting() =
        GsonMapperHelper.mapStringToDto(
            applicationSharedPreferenceManager.getString(SharedPreferenceKey.SETTING),
            Setting::class.java
        )
}