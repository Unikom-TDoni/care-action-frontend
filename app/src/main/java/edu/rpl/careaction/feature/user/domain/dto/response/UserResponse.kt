package edu.rpl.careaction.feature.user.domain.dto.response

import java.util.*
import com.google.gson.annotations.SerializedName
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.domain.entity.User

data class UserResponse(
    val content: Content
) {
    data class Content(
        val data: Data,

        @SerializedName("access_token")
        val token: String,

        @SerializedName("token_type")
        val tokenType: String,
    )

    data class Data(
        val height: Int,
        val weight: Int,
        val icon: String,
        val name: String,
        val email: String,
        val gender: String,
        val birthdate: Date,
    )
}

fun UserResponse.toUser(): User =
    User(
        DateUtility.generateAgeFromDate(content.data.birthdate),
        content.data.icon,
        content.data.height,
        content.data.weight,
        content.data.name,
        content.data.email,
        content.tokenType.plus(' ').plus(content.token),
        content.data.gender,
        content.data.birthdate,
    )
