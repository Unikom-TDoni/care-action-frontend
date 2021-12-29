package edu.rpl.careaction.feature.user.data.dao

import javax.inject.Inject
import edu.rpl.careaction.core.domain.dao.ApplicationSharedPreferences
import edu.rpl.careaction.feature.user.domain.entity.User

class UserLocalDataSource @Inject constructor(
    private val applicationSharedPreferences: ApplicationSharedPreferences
) {
    fun logout() =
        applicationSharedPreferences.clear()

    fun saveUser(user: User) =
        applicationSharedPreferences.saveUser(user)

    fun loadUser() =
        applicationSharedPreferences.loadUser()
}