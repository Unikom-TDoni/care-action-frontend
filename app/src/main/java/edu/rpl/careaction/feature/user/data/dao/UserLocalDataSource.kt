package edu.rpl.careaction.feature.user.data.dao

import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.core.data.ApplicationSharedPreferenceManager
import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.user.domain.entity.User
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val applicationSharedPreferenceManager: ApplicationSharedPreferenceManager
) {
    fun save(user: User) =
        applicationSharedPreferenceManager.store(
            mapOf(
                SharedPreferenceKey.USER to GsonMapperHelper.mapToJson(
                    user
                )
            )
        )

    fun load(): User? =
        GsonMapperHelper.mapStringToDto(
            applicationSharedPreferenceManager.getString(SharedPreferenceKey.USER), User::class.java
        )

    fun remove() =
        applicationSharedPreferenceManager.remove(setOf(SharedPreferenceKey.USER))
}