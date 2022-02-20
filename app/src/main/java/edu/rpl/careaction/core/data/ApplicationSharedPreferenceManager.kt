package edu.rpl.careaction.core.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.rpl.careaction.R
import edu.rpl.careaction.core.config.SharedPreferenceKey
import edu.rpl.careaction.module.storage.SharedPreferenceManager
import javax.inject.Inject

class ApplicationSharedPreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) : SharedPreferenceManager<SharedPreferenceKey>(
    context.resources.getString(R.string.app_name),
    context
) {
    fun getString(sharedPreferenceKey: SharedPreferenceKey) =
        sharedPreferences.getString(sharedPreferenceKey.name, String())

    fun getLong(sharedPreferenceKey: SharedPreferenceKey) =
        sharedPreferences.getLong(sharedPreferenceKey.name, 0)
}