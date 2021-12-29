package edu.rpl.careaction.feature.user.domain.dto.response

import java.util.*
import com.google.gson.annotations.SerializedName
import edu.rpl.careaction.feature.user.domain.entity.User

data class LoginRegisterResponse(
    val content: Content
) {
    data class Content(
        val data: Data,

        @SerializedName("access_token")
        val token: String
    )

    data class Data(
        val email: String,
        val gender: String,
        val birthdate: Date,
        val height: Int,
        val weight: Int,
        val name: String,
    )
}

fun LoginRegisterResponse.toUser(): User {
    return User(
        birthDate = content.data.birthdate,
        email = content.data.email,
        gender = content.data.gender,
        height = content.data.height,
        weight = content.data.weight,
        name = content.data.name,
        token = "Bearer "+content.token,
        age = 1,
    )
}