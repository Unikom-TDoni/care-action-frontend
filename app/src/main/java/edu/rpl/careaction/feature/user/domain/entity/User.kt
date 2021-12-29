package edu.rpl.careaction.feature.user.domain.entity

import java.util.*

data class User(
    val age: Int?,
    val height: Int?,
    val weight: Int?,
    val gender: String?,
    val birthDate: Date?,
    val name: String,
    val email: String,
    val token: String
){
    fun isLoggedIn() =
        token.isNotBlank()

    fun isHaveCompleteProfile() =
        weight == null || height == null || age == null || birthDate == null
}