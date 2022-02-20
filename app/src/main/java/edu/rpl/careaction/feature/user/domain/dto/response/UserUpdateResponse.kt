package edu.rpl.careaction.feature.user.domain.dto.response

import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.domain.entity.User
import java.util.*

data class UserUpdateResponse(
    val content: Content
) {
    data class Content(
        val data: Data,
    )

    data class Data(
        val height: Int,
        val weight: Int,
        val name: String,
        val gender: String,
        val picture: String,
        val birthdate: Date,
    )

    fun toUser(activeUser: User): User =
        User(
            DateUtility.generateAgeFromDate(content.data.birthdate),
            content.data.height,
            content.data.weight,
            content.data.name,
            activeUser.email,
            activeUser.token,
            content.data.gender,
            content.data.picture,
            content.data.birthdate,
        )
}
