package edu.rpl.careaction.core.domain.dao

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.rpl.careaction.R
import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.core.helper.GsonMapperHelper
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.storage.SharedPreferenceUtility
import javax.inject.Inject

class ApplicationSharedPreferences @Inject constructor(
    @ApplicationContext val context: Context
) : SharedPreferenceUtility<SharedPreferenceKey>(context.resources.getString(R.string.app_name), context) {

    fun saveUser(user: User) =
        store(mapOf(SharedPreferenceKey.USER to GsonMapperHelper.mapToJson(user)))

    fun loadUser(): User =
        GsonMapperHelper.mapToDto(sharedPreferences.getString(SharedPreferenceKey.USER.name, ""), User::class.java)

}