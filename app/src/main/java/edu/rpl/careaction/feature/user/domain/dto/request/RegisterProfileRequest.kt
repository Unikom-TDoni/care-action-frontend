package edu.rpl.careaction.feature.user.domain.dto.request

import java.util.*

data class RegisterProfileRequest(
    val weight: Int,
    val height: Int,
    val gender: String,
    val birthdate: Date,
)