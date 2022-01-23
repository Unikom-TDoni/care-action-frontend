package edu.rpl.careaction.feature.user.domain.entity

import java.util.*

data class User(
    val age: Int?,
    val height: Int?,
    val weight: Int?,
    val name: String,
    val email: String,
    val token: String,
    val gender: String?,
    val picture: String?,
    val birthDate: Date?,
) {
    fun isHaveCompleteProfile() =
        weight != null && height != null && gender != null && birthDate != null
}