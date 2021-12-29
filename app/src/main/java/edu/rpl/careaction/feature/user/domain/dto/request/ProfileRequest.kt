package edu.rpl.careaction.feature.user.domain.dto.request

import java.util.*

data class ProfileRequest(
    val gender: String,
    val birthdate: String,
    val weight: Int,
    val height: Int,
)